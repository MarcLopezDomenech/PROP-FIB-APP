package main.persistence;

import java.io.IOException;
import java.io.FileNotFoundException;  

/**
 * @class FpParser
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
     * @brief Llegeix el string que troba en el fitxer corresponent al path que rep en format propietari
     * @param path El path d'on es llegirà
     * @return String en format propietari representant el document llegit
     * @throws FileNotFoundException Excepció llençada si no es troba el fitxer corresponent al path donat
     */
    public String read(String path) throws FileNotFoundException {
        return readFromFile(path);
    }

    /**
     * @brief Funció per a escriure en format propietari un cert contingut en un fitxer al path corresponent
     * @param document string a escriure
     * @param path El path on s'escriurà
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
     */
    public void write(String document, String path) throws IOException{
        writeToFile(document, path);
    }
}
