package test.domain.documents;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
/**
 * @class InternalDocument (stub)
 * @brief Classe stub de InternalDocument
 * @author ariadna.cortes.danes
 */
public class InternalDocument {
    String content;
    Map<String,Integer> words;

    public InternalDocument(String content) {
        this.content = content;
        words = new HashMap<String,Integer>();
        words.put("Tres", 1);
        words.put("tristes", 2);
        words.put("tigres", 1);
        words.put("comen", 1);
        words.put("trigo",1);
        words.put("en", 1);
        words.put("un", 1);
        words.put("trigal", 1);
    }   
    public Map<String,Integer> getRelevantWords() {
        return words;
    }

    public Set<String> getRelevantKeyWords() {
        return words.keySet();
    }

    public int getTotalWords() {
        return words.size();
    }

    public void newContent(String content) {
        this.content = content;
    }
   

}
