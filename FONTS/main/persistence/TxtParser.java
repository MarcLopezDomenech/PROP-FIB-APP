package main.persistence;

import main.excepcions.ExceptionMissingTitleOrAuthor;

import java.io.File;
import java.io.FileNotFoundException;

import java.nio.charset.StandardCharsets;
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
     * @brief Llegeix el string que troba en el fitxer corresponent al path que rep en format txt
     * @param path El path d'on es llegirà
     * @return String en format propietari representant el document txt llegit
     * @throws FileNotFoundException Excepció llençada si no es troba el fitxer corresponent al path donat
     * @throws ExceptionMissingTitleOrAuthor Si autor o titol son buits
     */
    public String read(String path) throws FileNotFoundException, ExceptionMissingTitleOrAuthor {
        File myObj = new File(path);
       
        Scanner myReader = new Scanner(myObj, "UTF-8");
        if (!myReader.hasNextLine()) throw new ExceptionMissingTitleOrAuthor("un títol");
        String title = myReader.nextLine();
        if (title.equals("")) throw new ExceptionMissingTitleOrAuthor("un títol");
        if (!myReader.hasNextLine()) throw new ExceptionMissingTitleOrAuthor("un autor");
        String author = myReader.nextLine();
        if (author.equals("")) throw new ExceptionMissingTitleOrAuthor("un autor");
        String content = "";

        while (myReader.hasNextLine()) {
            content += myReader.nextLine() + "\n";
        }
        myReader.close();        
        return title + "@title@" + author + "@author@" + content + "@content@";
    }

    /**
     * @brief Funció per a escriure en format txt un cert contingut en un fitxer al path corresponent
     * @param document string a escriure
     * @param path El path on s'escriurà
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
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
