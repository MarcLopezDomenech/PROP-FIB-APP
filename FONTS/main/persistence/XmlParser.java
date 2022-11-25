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

    
    public String read(String path) throws FileNotFoundException{
        return "aaaa";
    }

    public void write(String document, String path) throws IOException {

        String[] information = document.split("@@@");
        if (information.length < 3) throw new IOException(); //Aixo equival a un internal error
        String title = information[0];
        String author = information[1];
        String content = information[2];

        String str = "<title>" + title + "<title>\n<author>" + author + "<author>\n<content>" + content + "<content>";
        writeToFile(str, path);
    }
}
