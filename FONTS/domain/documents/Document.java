package domain.documents;

import java.util.*;
import java.math.*;

import domain.documents.InternalDocument;

/**
 * @class Document 
 * @brief Classe que representa un document dins el sistema, identificat per un títol i un autor
 * Els documents tenen contingut, que pot ser txt o xml.
 * @author Ariadna Cortes Danes
 */
public class Document {

    private String content;
    private String author;
    private String title;
    private String originalFormat; //xml or txt
    private InternalDocument internalDoc;

    /**
     * @brief Constructora per defecte de document
     */
    Document(){}

    /**
     * @brief constructora de document
     * @param author autor del document creat
     * @param title titol del document creat
     * @param contingut del document creat
     * @param format format del document "txt" or "xml"
     * @throws InvalidFormat el format del document es invalid (no es txt o xml)
     */
    public Document(String author, String title, String content, String format) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.originalFormat = format;
        this.internalDoc = new InternalDocument(content);
    }

    /**
     * @brief Constructora del document quan fem un backUp
     * @param backUpInformation string per ser interpretat com a dades del document 
     */
    Document(String backUpInformation) {
        /*this.author = author;
        this.title = title;
        this.content = content;
        this.originalFormat = format;
        Map<String, Int> internalInformation = convert(backUpInformation)
        this.internalDoc = new InternalDocument(internalInformation);*/
    }

    /**
     * @brief get de l'atribut autor 
     */
    public String getAuthor() {
        return author;
    } 

  /**
     * @brief get de l'atribut titol 
     */
    public String getTitle() {
        return title;
    } 

     /**
     * @brief get de l'atribut contingut 
     */
    public String getContent() {
        return content;
    }

  /**
     * @brief get de l'atribut format (l'original que es va assignar al crear el document) 
     */
    public String getOriginalFormat() {
        return originalFormat;
    }

     /**
     * @brief 
     * 
    public InternalDocument getInternalDocument() {
        return internalDoc;
    }*/

     /**
     * @brief retorna el set de paraules clau del contingut del document   
     */
    public Set<String> getRelevantWords() {
        return internalDoc.getRelevantKeyWords();
    }

    /**
     * @brief sets a new content to the document
     * @details a new content will be analized and document internal data will be updated
     * @param newContent the new content of the document
     */
    public void setContent(String newContent) {
        content = newContent;
        internalDoc.analizeContent(newContent);
    }

    /**
     * @brief Valora com de semblants son els continguts de dos documents A i B
     * @details utilitzant l'algoritme tf-idf, calcula la rellevancia de A per a les paraules del contingut 
     * de B i la rellenvancia de B per a les paraules del contingut d'A
     * @param other Document amb el que es vol comparar el contingut
     * @return double que representa un index de semblança entre els documents A i B
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
     * @details Utilitza l'algoritme de tf-idf per a calcular la rellevancia del contingut del
     * document en relació a les paraules de la query. 
     * @pre el parametre query nomes conte paraules separades per espais (sense signes de puntuació, etc)
     * @param query conjunt de paraules en les que evaluar la rellevancia del document
     * @param num_docs total de documents del conjunt de documents al que pertany el document
     * @param presence mapa que guarda en quants documents apareix un paraula concreta: Map<paraula,cops>
     * @return double que quantifica la rellevancia d'una query per al contingut del document
     */
    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {

        String[] queryTerms = query.split(query);
        double tf_idf = termRelevance_tf_idf(queryTerms, num_docs, presence);

        return tf_idf*100;      //no es sobre 100 per ajuda a
    }


    /**
     * @brief Algorisme tf-idf 
     * @details Calcula com de rellevant es el contingut del document en relació a una serie de termes
     * @pre el parametre query nomes conte paraules separades per espais (sense signes de puntuació, etc)
     * @param terms termes dels que evaluar la rellevancia
     * @param num_docs total de documents del conjunt de documents al que pertany el document
     * @param presence mapa que guarda en quants documents apareix un paraula concreta: Map<paraula,cops>
     * @return double que quantifica la rellevancia d'una query per al contingut del document
     */
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
