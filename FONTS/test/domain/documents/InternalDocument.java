package test.domain.documents;

import java.util.*;

/**
 * @class InternalDocument (stub)
 * @brief Classe stub de InternalDocument
 * @author ariadna.cortes.danes
 */
public class InternalDocument {
    int id;

    public InternalDocument(String content, String language) {
        if (content.equals("Tres tristes, tristes tigres comen trigo en un trigal.")) id = 1;
        if (content.equals("Mi mama me mima")) id = 2;
        if (content.equals("Tres tristes palabras")) id = 3;
        if (content.equals("Tres trigal en un tigre triste")) id = 4;
        if (content.equals("A cup of cafe con leche in Plaza Mayor")) id = 5;
    } 

    public Map<String,Integer> getRelevantWords() {
        Map<String,Integer> words = new HashMap<>();
        if (id == 1) {
            words.put("Tres", 1);
            words.put("tristes", 2);
            words.put("tigres", 1);
            words.put("comen", 1);
            words.put("trigo", 1);
            words.put("trigal", 1);
        }
        else if (id == 2) {
            words.put("Mi", 1);
            words.put("mama", 1);
            words.put("me", 1);
            words.put("mima", 1);
        }
        else if (id == 3) {
            words.put("Tres", 1);
            words.put("tristes", 1);
            words.put("palabras", 1);
        }
        else if (id == 4) {
            words.put("Tres", 1);
            words.put("trigal", 1);
            words.put("triste", 1);
            words.put("tigre", 1);
        }       

        return words;
    }

    public Set<String> getRelevantKeyWords() {
        Set<String> words = new HashSet<>();
        if (id == 1) {
            String keywords[] = {"tristes", "tigres", "comen", "trigo", "trigal"};
            words.addAll(Arrays.asList(keywords));
        }
        else if (id == 2) {
            String keywords[] = {"mama", "mima"};
            words.addAll(Arrays.asList(keywords));
        }
        else if (id == 3) {
            String keywords[] = {"tristes", "palabras"};
            words.addAll(Arrays.asList(keywords));
        }
        else if (id == 4) {
            String keywords[] = {"triste", "tigre", "trigal"};
            words.addAll(Arrays.asList(keywords));
        }
        return words;
    }

    public int getTotalWords() {
        if (id == 1) return 6;
        if (id == 2) return 2;
        if (id == 3) return 2;
        if (id == 4) return 3;
        else return 0;
    }

    public void newContent(String content, String language) {
    }
}
