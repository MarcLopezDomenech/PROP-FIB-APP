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
 * @class Parser
 * @brief Classe abstracte de la que heredaran els diferents parsers responsables de la lectura i escriptura de fitxers en cada format
 * @author ariadna.cortes.danes
 */
public abstract class Parser {
    
    public Parser() {

    }

    /**
     * @brief Constructora de la classe
     */
    public abstract String read(String path) throws FileNotFoundException;

    /**
     * @brief Constructora de la classe
     */
    public abstract void write(String document, String Path) throws IOException;

    /**
     * @brief Constructora de la classe
     */
    public String readFromFile(String path) throws FileNotFoundException{
        File myObj = new File(path);
        String data = "";
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            data += myReader.nextLine() + "\n";
        }
        myReader.close();
        return data;
    }
       

    /**
     * @brief Constructora de la classe
     */
    public void writeToFile(String str, String path) throws IOException {
        FileWriter myWriter = new FileWriter(path);
        myWriter.write(str);
        myWriter.close();
    }

    /**
     * @brief Constructora de la classe
     */
    public void saveSystem(Set<String> docs, Set<String> exprs, String path) throws IOException{
        String data = "Documents\n";
        for(String doc : docs) {
            data += "{{" + doc + "}} \n";
        }
        data += "Expressions\n";
        for(String expr : exprs) {
            data += "{{" + expr + "}} \n";
        }
        writeToFile(data, path);
    }

    public void restoreSystem(Set<String> docs, Set<String> exprs, String path) throws FileNotFoundException{
        File myObj = new File(path);
        Scanner myReader = new Scanner(myObj);
        boolean expressions = false;
        String data = myReader.nextLine();
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            if (data.equals("Expressions")) expressions = true;
            else {
                data = data.substring(2, data.length() - 3);
                if (!expressions) docs.add(data);
                else exprs.add(data);
            }
        }
        myReader.close();
    }
}
