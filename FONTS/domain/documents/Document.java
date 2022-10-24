package domain.documents;

import java.util.*;
import java.math.*;

import domain.documents.InternalDocument;

/**
 * @class Document 
 * @brief This class represents a document in our system, identified by a title and an author.
 * Documents have a content that can be either txt or xml.
 * @author Ariadna Cortes Danes
 */
public class Document {

    private String content;
    private String author;
    private String title;
    private String originalFormat; //xml or txt
    private InternalDocument internalDoc;

    public static void main(String[] args) {
        System.out.println("Hello World \n wooooorld");
    }
    /**
     * Default constructor
     */
    Document(){}

    /**
     * Normal constructor of a document
     */
    public Document(String author, String title, String content, String format) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.originalFormat = format;
        this.internalDoc = new InternalDocument(content);
    }

    /**
     * Constructor of a document when a back-up is done
     */
    Document(String backUpInformation) {
        /*this.author = author;
        this.title = title;
        this.content = content;
        this.originalFormat = format;
        Map<String, Int> internalInformation = convert(backUpInformation)
        this.internalDoc = new InternalDocument(internalInformation);*/
    }

    public String getAuthor() {
        return author;
    } 

    public String getTitle() {
        return title;
    } 

    public String getContent() {
        return content;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public InternalDocument getInternalDocument() {
        return internalDoc;
    }

    public Set<String> getRelevantWords() {
        return internalDoc.getRelevantKeyWords();
    }

    public void setContent(String newContent) {
        content = newContent;
        internalDoc.analizeContent(newContent);
    }

    /**
     * @brief Valora com de semblants son els continguts de dos documents
     * @param other Document amb el que es vol comparar el contingut
     * @return Enter del 1 al 100 entes com el percentatge de semblança entre els dos documents
     */
    public double compare(Document other, Integer num_docs, Map<String,Integer> presence) {

        Set<String> terms1 = other.getRelevantWords();
        Set<String> terms2 = this.getRelevantWords();
        String terms1Array[] = new String[terms1.size()];
        String terms2Array[] = new String[terms2.size()];
        terms1.toArray(terms1Array);
        terms2.toArray(terms2Array);

        double tf_idf1 = termRelevance_tf_idf(terms1Array, num_docs, presence);
        double tf_idf2 = other.termRelevance_tf_idf(terms2Array, num_docs,presence);

        return Math.sqrt(tf_idf1*tf_idf2)*100;
    }

    /**
     * @brief Valora com de rellevant es el contingut del document donada una query
     * @return double que quantifica la rellevancia d'una query per al contingut del document
     */
    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {

        String[] queryTerms = query.split(query);
        double tf_idf = termRelevance_tf_idf(queryTerms, num_docs, presence);

        return tf_idf*100;      //no es sobre 100 per ajuda a
    }

    public double termRelevance_tf_idf(String[] terms, Integer num_docs, Map<String,Integer> presence) {
        double tf_idf = 0;
        int totalTerms = internalDoc.getTotalWords();
        Map<String,Integer> relevantTerms = internalDoc.getRelevantWords(); 
        for (String term : terms) {
            if (relevantTerms.containsKey(term)) {
                tf_idf += (relevantTerms.get(term)/totalTerms) * Math.log10(num_docs/presence.get(term));
            }
        }
        return tf_idf;
    }

} 
