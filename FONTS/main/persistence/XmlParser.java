package main.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @class TxtParser
 * @brief Classe responsable de la lectura i escriptura de fitxers en format txt
 * @author ariadna.cortes.danes
 */
public class XmlParser extends Parser {
    
    /**
     * @brief Constructora de la classe
     */
    public XmlParser() {

    }


    public String read(String path) throws FileNotFoundException {
        String input = readFromFile(path);
        String[] information = input.split("<title>");
        String title = information[1];
        input = information[2];
    
        //obtenir el autor
        information = input.split("<author>");
        String author = information[1];
        input = information[2];

        //Obtenir el contingut 
        information = input.split("<content>");
        String content = information[1];

        return  title + "@title@" + author + "@author@" + content + "@content@";
    }

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

        String str = "<title>" + title + "<title>\n<author>" + author + "<author>\n<content>" + content + "<content>";
        writeToFile(str, path);
    }
}
