package main.domain.documents;

import java.util.*;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;
import main.domain.documents.InternalDocument;         //Per fer tests unitaris, canviar aquest import a test.domain.documents.InternalDocument (on està l'stub)

/**
 * @class Document 
 * @brief Classe que representa un document dins el sistema, identificat per un títol i un autor
 * Els documents tenen contingut, que pot ser txt o xml.
 * @author ariadna.cortes.danes
 */
public class Document {

    private String content;
    private String author;
    private String title;
    private String originalFormat;          //xml or txt
    private String language;                // ca, es, en
    private InternalDocument internalDoc;

    /**
     * @brief Constructora per defecte de document
     */
    public Document(){}

    /**
     * @brief Constructora de document
     * @param author Autor del document creat
     * @param title Titol del document creat
     * @param content Contingut del document creat
     * @param format Format del document: "txt" or "xml"
     * @throws ExceptionInvalidFormat El format del document es invalid (no es txt o xml)
     */
    public Document(String author, String title, String content, String language, String format) throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        this.author = author;
        this.title = title;
        this.content = content;
        if (!"ca".equals(language) && !"en".equals(language) && !"es".equals(language)) throw new ExceptionInvalidLanguage(language);
        this.language = language;
        if (!format.equals("txt") && !format.equals("xml")) throw new ExceptionInvalidFormat(format);
        this.originalFormat = format;
        this.internalDoc = new InternalDocument(content, language);
    }

    /**
     * @brief constructora de document sense format especific
     * @param author autor del document creat
     * @param title titol del document creat
     * @param content del document creat
     */
    public Document(String author, String title, String content, String language) throws ExceptionInvalidLanguage {
        if (!"ca".equals(language) && !"en".equals(language) && !"es".equals(language)) throw new ExceptionInvalidLanguage(language);
        this.author = author;
        this.title = title;
        this.content = content;
        this.language = language;
        this.originalFormat = null;
        this.internalDoc = new InternalDocument(content, language);
    }

    /**
     * @brief Constructora del document utilitzada al fer un back-up de l'aplicació
     * @details String backUpInformation conte les etiquetes author, title, format i content,
     * que identifiquen cada un dels atributs del document. 
     * @param backUpInformation String que serà interpretat com les dades a recuperar del document 
     */
    public Document(String backUpInformation) {
        //Aquest metode encara  no esta implementat, pertany als casos d'ús associats a persistència
    }

    /**
     * @brief Getter de l'atribut autor 
     */
    public String getAuthor() {
        return author;
    } 

  /**
     * @brief Getter de l'atribut titol 
     */
    public String getTitle() {
        return title;
    } 

     /**
     * @brief Getter de l'atribut contingut 
     */
    public String getContent() {
        return content;
    }

    /**
     * @brief Getter de l'atribut format (format original que es va assignar al crear el document) 
     */
    public String getOriginalFormat() {
        return originalFormat;
    }

    /**
     * @brief Getter de l'atribut language 
     */
    public String getLanguage() {
        return language;      
    }

     /**
     * @brief Retorna el set de paraules clau del contingut del document   
     * @details Paraules claus son totes aquelles paraules que apareixen en el contingut del document (sense repeticions)
     */
    public Set<String> getRelevantWords() {
        return internalDoc.getRelevantKeyWords();
    }

    /**
     * @brief Actualitza el contingut del document
     * @details S'assignara un nou contingut al document, que s'analitzara i modificara les dades del document en conseqüencia
     * @param newContent El nou contingut a ser assignat al document
     */
    public void setContent(String newContent) {
        content = newContent;
        internalDoc.newContent(newContent, language);
    }

    /**
     * @brief Actualitza l'idioma del document
     * @details S'assignara un nou idioma al document
     * @param newLanguage El nou idioma a ser assignat al document
     */
    public void setLanguage(String newLanguage) throws ExceptionInvalidLanguage {
        if (!"ca".equals(newLanguage) && !"en".equals(newLanguage) && !"es".equals(newLanguage)) throw new ExceptionInvalidLanguage(newLanguage);
        language = newLanguage;
        internalDoc.newContent(content, newLanguage);
    }

    /**
     * @brief Valora com de semblants son els continguts de dos documents A i B
     * @details Utilitzant l'algoritme de pesos de tf-idf, calcula la rellevancia de A per a les paraules del contingut 
     * de B i la rellenvancia de B per a les paraules del contingut d'A
     * @param other Document amb el que es vol comparar el contingut
     * @param num_docs nombre total de documents del sistema
     * @param presence Mapa que ens diu en quants documents apareix 
     * @return Nombre que representa un index de semblança entre els documents A i B
     */
    public double compare_tf_idf(Document other, Integer num_docs, Map<String,Integer> presence) {
        if (!language.equals(other.getLanguage())) return 0;

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
     * @brief Valora com de semblants son els continguts de dos documents A i B
     * @details Utilitzant l'algoritme de tf escalat logaritmicament, calcula la rellevancia de A per a les paraules del contingut 
     * de B i la rellenvancia de B per a les paraules del contingut d'A
     * @param other Document amb el que es vol comparar el contingut
     * @return Nombre que representa un index de semblança entre els documents A i B
     */
    public double compare_tf_boolean(Document other) {
        if (!language.equals(other.getLanguage())) return 0;

        Set<String> terms1 = other.getRelevantWords();
        String terms1Array[] = new String[terms1.size()];
        terms1.toArray(terms1Array);

        double tf_1 = termRelevance_tf_boolean(terms1Array);
        return tf_1;
    }
    /**
     * @brief Valora com de rellevant es el contingut del document donada una query
     * @details Utilitza l'algoritme de tf-idf per a calcular la rellevancia del contingut del document en relació a les paraules de la query. 
     * @pre El parametre query nomes conte paraules separades per espais (sense signes de puntuació, etc)
     * @param query Conjunt de paraules en les que evaluar la rellevancia del document
     * @param num_docs Total de documents del conjunt de documents al que pertany el document
     * @param presence Mapa que guarda en quants documents del sistema apareix un paraula concreta: Map<paraula,cops>
     * @return Nombre que quantifica la rellevancia d'una query per al contingut del document
     */
    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {
        String[] queryTerms = query.split(" ");
        double tf_idf = termRelevance_tf_idf(queryTerms, num_docs, presence);

        return tf_idf*100;
    }


    /**
     * @brief Implementacio de l'algorisme tf-idf 
     * @details Calcula com de rellevant es el contingut del document en relació a una serie de termes
     * @pre El parametre terms es un conjunt de paraules atomiques (sense espais ni signes de puntuació, etc.)
     * @param terms Termes dels que evaluar la rellevancia
     * @param num_docs Total de documents del conjunt de documents al que pertany el document
     * @param presence Mapa que guarda en quants documents del sistema apareix un paraula concreta: Map<paraula,cops>
     * @return Nombre que quantifica la rellevancia d'una serie de termes per al contingut del document
     */
    private double termRelevance_tf_idf(String[] terms, Integer num_docs, Map<String,Integer> presence) {
        double tf_idf = 0;
        double totalTerms = (double) internalDoc.getTotalWords();
        Map<String,Integer> relevantTerms = internalDoc.getRelevantWords(); 
        for (String term : terms) {
            if (relevantTerms.containsKey(term)) {
                tf_idf += (relevantTerms.get(term)/totalTerms) * Math.log10((double) num_docs/presence.get(term));
            }
        }
        return tf_idf;
    }

    /**
     * @brief Implementacio de l'algorisme tf-idf 
     * @details Calcula com de rellevant es el contingut del document en relació a una serie de termes
     * @pre El parametre terms es un conjunt de paraules atomiques (sense espais ni signes de puntuació, etc.)
     * @param terms Termes dels que evaluar la rellevancia
     * @param num_docs Total de documents del conjunt de documents al que pertany el document
     * @param presence Mapa que guarda en quants documents del sistema apareix un paraula concreta: Map<paraula,cops>
     * @return Nombre que quantifica la rellevancia d'una serie de termes per al contingut del document
     */
    private double termRelevance_tf_boolean(String[] terms) {
        double tf_bool = 0;
        Map<String,Integer> relevantTerms = internalDoc.getRelevantWords(); 
        for (String term : terms) {
            if (relevantTerms.containsKey(term)) tf_bool++;
        }
        return tf_bool;
    }

} 
