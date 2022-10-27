package test.domain.documents;

import java.util.*;

/**
 * @class Documents (stub)
 * @brief Classe stub de Document
 * @author pau.duran.manzano
 */
public class Document {

    public Document() {}

    public Document(String title, String author, String content) {}

    public Document(String title, String author, String content, String format) {}

    public String getContent() {
        return "Contingut inventat per poder fer tests.";
    }

    public void setContent(String content) {}

    public Set<String> getRelevantWords() {
        Set<String> s1 = new HashSet<>(Arrays.asList("paraula", "hola", "que", "tal"));
        Set<String> s2 = new HashSet<>(Arrays.asList("paraula", "hola", "que", "tal"));
        Set<String> s3 = new HashSet<>(Arrays.asList("això", "és", "una", "classe", "stub"));
        Set<String> s4 = new HashSet<>(Arrays.asList("per", "fer", "tests"));
        Set<String> s5 = new HashSet<>(Arrays.asList());

        List<Set<String>> relevantWords  = Arrays.asList(s1, s2, s3, s4, s5);
        int r = new Random().nextInt(5);
        System.out.println("Estem a la funció getRelevantWords i es retorna " + r);
        return relevantWords.get(r);
    }

    public double compare(Document other, Integer num_docs, Map<String,Integer> presence) {
        return Math.random()*100;
    }

    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {
        return Math.random()*100;
    }


}
