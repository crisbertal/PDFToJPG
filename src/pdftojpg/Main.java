/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdftojpg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author crisber
 */
public class Main {

    /**
     * Toma un fichero PDF y lo convierte a JPG.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main programa = new Main();
        programa.iniciar();
    }

    public void iniciar() {
        //Indica el directorio donde se encuentran los ficheros.
        Scanner teclado = new Scanner(System.in);
        System.out.println("Introduce la ruta del directorio que contiene los PDF: ");
        String ruta = teclado.nextLine();
        System.out.printf("La ruta introducida ha sido: %s%n", ruta);
        
        File directorioPrueba = new File(ruta);
        File[] ficheros = directorioPrueba.listFiles();

        // Comprueba que todos los ficheros del directorio son PDF.
        int contadorNoPDF = 0;
        for (File fichero : ficheros) {
            if (!esPDF(fichero)) {
                contadorNoPDF++;
            }
        }
        
        // Si algun fichero no es PDF se termina la ejecucion del programa.
        if (contadorNoPDF > 0) {
            System.out.printf("Hay %d ficheros en el directorio que no son PDF.%n", contadorNoPDF);
        } else {
            for (File fichero : ficheros) {
                convertirFichero(fichero);
            }
        }
        System.out.println("Fin de la ejecucion.");
    }

    /**
     * A partir del nombre del fichero PDF, crea el nombre del fichero con la
     * extension JPG.
     *
     * @param fichero fichero que se va a transformar
     * @return String con el nombre del fichero y la extensión .jpg.
     */
    private String cambiarExtensionJPG(File fichero) {
        String nombrePDF = fichero.getAbsolutePath();
        String sinTipo = nombrePDF.substring(0, nombrePDF.length() - 4);
        String nombreJPG = sinTipo.concat(".jpg");
        return nombreJPG;
    }

    /**
     * Comprueba que el fichero es PDF.
     *
     * @param fichero fichero que se desea comprobar.
     * @return valor booleano.
     */
    private boolean esPDF(File fichero) {
        String nombre = fichero.getName();
        String extension = nombre.substring(nombre.length() - 4);
        boolean esPDF = extension.equals(".pdf");
        return esPDF;
    }

    /**
     * Convierte el fichero PDF introducido en un fichero JPG.
     * @param ficheroPDF fichero PDF a convertir.
     */
    public void convertirFichero(File ficheroPDF) {
        try {
            /*
            Clase que maneja los ficheros PDF de la clase PDFBox. Además
            carga el fichero PDF para poder usar los métodos de la clase con
            el método load(File fichero). El método load devuelve un objeto
            static de clase PDDocument (por eso no es necesario instanciarlo).
             */
            PDDocument doc = PDDocument.load(ficheroPDF);
            /*
            La clase PDFRenderer es usada para transformar el objeto
            PDDocument en un objeto BufferedImage. Esta clase es nativa de 
            Java, del paquete java.awt.image. Sirve para que Java pueda leer 
            una imagen raster.
             */
            PDFRenderer renderer = new PDFRenderer(doc);
            /*
            Para pasar el objeto renderer a BufferedImage se usa el método
            renderImage y el número de la página a transformar.
             */
            BufferedImage image = renderer.renderImage(0);
            /*
            La clase ImageIO es utilizada para leer o escribir imagenes.
            En este caso será usada para escribir la imagen obtenida del PDF
            en una imagen del formato especificado. Los parámetros del método
            write son (objeto imagen renderizada que ne este caso es el
            BufferedImage, formato en el que se va a escribir la imagen, ruta 
            del fichero en el cual se va a escribir la imagen.
             */
            ImageIO.write(image, "JPG", new File(cambiarExtensionJPG(ficheroPDF)));
            /*
            Si se ha acabado la operación con el fichero PDF, se debe cerrar
            el flujo de lectura.
             */
            doc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
