package domain.documents;

//External include
import java.util.*;
//Project includes

/**
 * @class InternalDocument 
 * @brief This class stores the interal data that the system saves for each
 * document, that is used to compare documents and search content similarities 
 * @author Ariadna Cortes Danes
 */
public class InternalDocument {

    private Map<String, Integer>  relevantWords;
    private int totalWords;

    /**
     * Default constructor
     */
    InternalDocument(){
        totalTerms = 0;
    }

    /**
     * Normal constructor of a document
     */
    InternalDocument(String content) {
        analizeContent(content);
    }   

    InternalDocument(Map<String,Integer> relevantWords) {
        this.relevantWords = relevantWords;
    } 

    /**
     * Get del atribut words
     */
    public Map<String,Integer> getRelevantWords() {
        return relevantWords;
    }

    /**
     * Get del atribut words
     */
    public Set<String> getRelevantKeyWords() {
        return relevantWords.keySet();
    }

    /**
     * Get del atribut words
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * @brief Extract the data corresponding to the contents recieved
     * @details his function initializes/actualizes a map<keyword, times>
     * where times is the number of repetitions of the keyword in the content
     * @param Content the content to analize
     */  
    public void analizeContent (String content) {
        Map<String,Integer> words = new HashMap<String,Integer>();
        String[] splited = content.split("( )|(.)|(,)|(!)|(?)");
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
