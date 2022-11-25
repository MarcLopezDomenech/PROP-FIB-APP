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
     * @brief Constructora de la classe
     */
    public String read(String path) throws FileNotFoundException{
        return readFromFile(path);
    }

    /**
     * @brief Constructora de la classe
     */
    public void write(String document, String path) throws IOException {

        String[] information = document.split("@@@");
        if (information.length < 3) throw new IOException(); //Aixo equival a un internal error
        String title = information[0];
        String author = information[1];
        String content = information[2];

        String str = title + "\n" + author + "\n" + content;
        writeToFile(str, path);
    }
}
