package main.persistence;

import main.excepcions.ExceptionInvalidCharacter;
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
 * @class Parser
 * @brief Classe abstracte de la que heredaran els diferents parsers responsables de la lectura i escriptura de fitxers en cada format
 * @author ariadna.cortes.danes
 */
public abstract class Parser {

    /**
     * @brief Constructora de la classe
     */
    public Parser() {

    }

    /**
     * @brief Llegeix el string que troba en el fitxer corresponent al path que rep
     * @param path El path d'on es llegirà
     * @return Contingut del path formatejar segons correspongui
     * @throws FileNotFoundException Excepció llençada si no es troba el fitxer corresponent al path donat
     */
    public abstract String read(String path) throws FileNotFoundException, ExceptionInvalidCharacter, ExceptionMissingTitleOrAuthor;

    /**
     * @brief Funció per a escriure un cert contingut en un fitxer al path corresponent
     * @param document string a escriure
     * @param path El path on s'escriurà
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
     */
    public abstract void write(String document, String path) throws IOException;


    /**
     * @brief Llegeix el string que troba en el fitxer corresponent al path que rep
     * @param path El path d'on es llegirà
     * @return Contingut del path
     * @throws FileNotFoundException Excepció llençada si no es troba el fitxer corresponent al path donat
     */
    public String readFromFile(String path) throws FileNotFoundException{
        File myObj = new File(path);
        String data = "";
        Scanner myReader = new Scanner(myObj, "UTF-8");
        while (myReader.hasNextLine()) {
            data += myReader.nextLine() + "\n";
        }
        myReader.close();
        return data;
    }


    /**
     * @brief Escriu el string que rep en el fitxer corresponent al path que rep
     * @param str El que s'escriurà
     * @param path El path on s'escriurà
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
     */
    public void writeToFile(String str, String path) throws IOException {
        FileWriter myWriter = new FileWriter(path, StandardCharsets.UTF_8);
        myWriter.write(str);
        myWriter.close();
    }

    /**
     * @brief Crea un còpia de seguretat del sistema
     ** @param docs Conjunt de strings corresponents a la representacióo iterna dels documents del sistema
     * @param exprs Conjunt de strings corresponents als identificadors de les expressions del sistema
     * @param path Path on es guardarà la copia de seguretat.
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
     */
    public void saveSystem(Set<String> docs, Set<String> exprs, String path) throws IOException{
        String data = "";
        for(String doc : docs) {
            data += doc + "<doc>\n";
        }
        for(String expr : exprs) {
            data += expr + "<expr>\n";
        }
        writeToFile(data, path);
    }

    /**
     * @brief Recupera l'estat del sistema en basa a una còpia de seguretat del sistema
     * @param docs Conjunt de strings corresponents a la representacióo iterna dels documents del sistema
     * @param exprs Conjunt de strings corresponents als identificadors de les expressions del sistema
     * @param path Path d'on es carregarà la copia de seguretat.
     * @throws FileNotFoundException Excepció que en llença quan no es troba cap còpia de seguretat
     */
    public void restoreSystem(Set<String> docs, Set<String> exprs, String path) throws FileNotFoundException{
        File myObj = new File(path);
        Scanner myReader = new Scanner(myObj, "UTF-8");
        String input = "";
        String content = "";
        while (myReader.hasNextLine()) {
            input = myReader.nextLine();

            if (input.endsWith("<doc>")) {
                input = input.substring(0, input.length() - 5); 
                content += input;
                docs.add(content);
                content = "";
            }
            else if (input.endsWith("<expr>")) {
                input = input.substring(0, input.length() - 6); 
                content += input;
                exprs.add(content);
                content = "";
            } else {
                content += input + "\n";
            }           
        }
        myReader.close();
    }
}
