package test.domain.documents;

import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;

import java.util.*;

/**
 * @class Documents (stub)
 * @brief Classe stub de Document
 * @author pau.duran.manzano
 */
public class Document {
    private String content;
    private String language;

    public Document() {}

    public Document(String author, String title, String content, String language) throws ExceptionInvalidLanguage {
        if (!"ca".equals(language) && !"en".equals(language) && !"es".equals(language)) throw new ExceptionInvalidLanguage(language);
        this.content = content;
        this.language = language;
    }

    public Document(String author, String title, String content, String language, String format) throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {this.content = content;}

    public Set<String> getRelevantWords() {
        Set<String> relevantWordsFake = new HashSet<>();
        for (Character c : content.toCharArray()) {
            String s = c.toString();
            if (!" ".equals(s)) relevantWordsFake.add(s);
        }
        return relevantWordsFake;
    }

    public double compare_tf_idf(Document other, Integer num_docs, Map<String,Integer> presence) {
        Integer r = other.content.length() - content.length();
        if (r < 0) return -r;
        else return r;
    }
    public double compare_tf_boolean(Document other) {
        Integer r = other.content.length() - content.length();
        if (r < 0) return -r;
        else return r;
    }
    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {
        Integer r = query.length() - content.length();
        if (r < 0) return -r;
        else return r;
    }

    public void setLanguage(String language) {this.language = language;}
    public String getLanguage() {return language;}

}
