package main.persistence;

import java.io.IOException;
import java.io.FileNotFoundException; 

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
        return readFromFile(path);
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
