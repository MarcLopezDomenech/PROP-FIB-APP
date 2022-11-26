package main.persistence;

import java.io.File;  
import java.io.FileNotFoundException;  

import java.nio.file.Path;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
/**
 * @class TxtParser
 * @brief Classe responsable de la lectura i escriptura de fitxers en format txt
 * @author ariadna.cortes.danes
 */
public class TxtParser extends Parser {
     /**
     * @brief Constructora de la classe
     */
    public TxtParser() {

    }

    /**
     * @brief Llegeix el contingut d'un fitxer en txt i el retorna en format propietari del sistema
     */
    public String read(String path) throws FileNotFoundException{
        File myObj = new File(path);
       
        Scanner myReader = new Scanner(myObj);
        String title = myReader.nextLine();
        String author = myReader.nextLine();
        String content = "";

        while (myReader.hasNextLine()) {
            content += myReader.nextLine() + "\n";
        }
        myReader.close();
        return title + "@title@" + author + "@author@" + content + "@content@";
    }

    /**
     * @brief Escriu en document inicialment en format propietari en format txt en el path especificat
     */
    public void write(String document, String path) throws IOException {
        //Obtenir el titol
        String[] information = document.split("@title@");
        String title = information[0];
        document = information[1];
    
        //obtenir el autor
        information = document.split("@author@");
        String author = information[0];
        document = information[1];

        //Obtenir el contingut 
        information = document.split("@content@");
        String content = information[0];

        String str = title + "\n" + author + "\n" + content;
        writeToFile(str, path);
    }
}
