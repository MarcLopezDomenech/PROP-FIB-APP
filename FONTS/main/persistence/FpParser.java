package main.persistence;

import java.io.IOException;
import java.io.FileNotFoundException;  

/**
 * @class TxtParser
 * @brief Classe responsable de la lectura i escriptura de fitxers en format propietari
 * @author ariadna.cortes.danes
 */
public class FpParser extends Parser {

     /**
     * @brief Constructora de la classe
     */
    public FpParser() {

    }

    /**
     * @brief Constructora de la classe
     */
    public String read(String path) throws FileNotFoundException {
        return readFromFile(path);
    }

    /**
     * @brief Constructora de la classe
     */
    public void write(String document, String path) throws IOException{
        writeToFile(document, path);
    }
}
