package main.domain.documents;

//External include
import java.util.*;
//Project includes

/**
 * @class InternalDocument 
 * @brief Classe que representa les dades internes que el sistema guarda per a cada document 
 * Aquesta informacio es utilitzada per a comparar documents i per a evaluar rellevancia  
 * @author Ariadna Cortes Danes
 */
public class InternalDocument {

    private Map<String, Integer>  relevantWords;
    private int totalWords;

     /**
     * @brief Constructora per defecte de InternalDocument
     */
    InternalDocument(){
        analizeContent("");
    }

    /**
     * @brief Constructora de InternalDocument
     * @param content contingut del que s'ha de guardar la representacio
     */
    InternalDocument(String content) {
        analizeContent(content);
    }   

    /**
     * @brief Constructora per a fer backUps de InternalDocument
     * @param relevantWords Map<paraula,cops> que correspon a l'analisi del contingut d'un document
     * @param numWords nombre total de paraules que te el contingut del document
     */
    InternalDocument(Map<String,Integer> relevantWords, int totalWords) {
        this.relevantWords = relevantWords;
        this.totalWords = totalWords;
    } 

    /**
     * @brief get de l'atribut relevantWords
     */
    public Map<String,Integer> getRelevantWords() {
        return relevantWords;
    }

    /**
     * @brief get del set de claus de l'atribut relevantWords
     */
    public Set<String> getRelevantKeyWords() {
        return relevantWords.keySet();
    }

    /**
     * @brief get de l'atribut totalWords (nombre total de paraules del contingut)
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * @brief Analitzar les dades que el sistema guarda pel contingut rebut
     * @details aquesta funcio inicialitza/actualitza el contingut de revelantWords
     * (Map<paraula,cops>) i de totalWords (nombre total de paraules del contingut)
     * @param Content el contingut a analitzar
     */  
    public void analizeContent (String content) {
        Map<String,Integer> words = new HashMap<String,Integer>();
        String[] splited = content.split("^\\w");
        totalWords = splited.length;
        
        for (String word : splited) {
            if (words.containsKey(word)) words.replace(word, 1 + words.get(word));
            else words.put(word, 1);
        }
        relevantWords = words;
    }

    //IO (no cal implementar de moment)

    /**
     * @brief Writes the data stored in the map<keywords,times> in a printable 
     * format in order to save information
     * @details 
     * @return Un format de text que emmagatzema les dades del internalDocument
     */  
    public String writeBackUp () {
        return "Aqui va el map escrit en format {(key1, valor1),(key2,valor2)}";
    }
} 
