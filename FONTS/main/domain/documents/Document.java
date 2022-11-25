package main.domain.documents;

import java.util.*;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;
import main.domain.documents.InternalDocument;         //Per fer tests unitaris, canviar aquest import a test.domain.documents.InternalDocument (on està l'stub)

/**
 * @class Document 
 * @brief Classe que representa un document dins el sistema, identificat per un títol i un autor
 * Els documents tenen contingut, que pot ser txt o xml, un idioma (ca,es,en) i una representació de dades interna del sistema
 * @author ariadna.cortes.danes
 */
public class Document {

    /**@brief El títol del document */
    private String content;

    /**@brief L'autor del document */
    private String author;

    /**@brief El contingut del document */
    private String title;

    /**@brief El format original del document 
     * @invariant Pot prendre per valors "xml" o "txt"
     */
    private String originalFormat;  
    
    /**@brief L'idioma del document 
     * @invariant Pot prendre per valors "ca", "es" o "en"
     */
    private String language;      
    
    /**@brief Representactió interna del document en el sistema */
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
     * @param format Format del document
     * @param language Idioma del document
     * @throws ExceptionInvalidFormat El format del document es invalid (no es txt o xml)
     * @throws ExceptionInvalidLanguage L'idioma del document no es vàlid (no es ca, es o en)
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
     * @brief Constructora de document sense format especific
     * @param author Autor del document creat
     * @param title Titol del document creat
     * @param content Del document creat
     * @param language Idioma del document
     * @throws ExceptionInvalidLanguage L'idioma del document no es vàlid (no es ca, es o en)
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
     * @details Paraules claus son totes aquelles paraules que apareixen en el contingut del document (sense repeticions i sense stop words)
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
     * @details Utilitzant l'algoritme de pesos de tf-idf, calcula la rellevancia de A per a les paraules del contingut de B i la rellenvancia de B per a les paraules del contingut d'A. Dos documents en idiomes diferents, es consideraran de rellevància 0. 
     * @param other Document amb el que es vol comparar el contingut
     * @param num_docs Nombre total de documents del sistema
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
     * @details Utilitzant l'algoritme de tf booleà, calcula la rellevancia de A per a les paraules del contingut de B. Dos documents en idiomes diferents, es consideraran de rellevància 0. 
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
     * @pre El paràmetre query només conté paraules separades per espais (sense signes de puntuació, etc)
     * @param query Conjunt de paraules en les que avaluar la rellevancia del document
     * @param num_docs Total de documents del conjunt de documents al que pertany el document
     * @param presence Mapa que guarda en quants documents del sistema apareix un paraula concreta: Map<paraula,cops>
     * @return Index numèric que quantifica la rellevancia d'una query per al contingut del document
     */
    public double queryRelevance(String query, Integer num_docs, Map<String,Integer> presence) {
        query = query.toLowerCase();
        String[] queryTerms = query.split(" ");
        double tf_idf = termRelevance_tf_idf(queryTerms, num_docs, presence);

        return tf_idf*100;
    }


    /**
     * @brief Implementacio de l'algorisme tf-idf 
     * @details Calcula com de rellevant es el contingut del document en relació a una sèrie de termes mitjançant l'algorisme tf-idf 
     * @pre El paràmetre terms es un conjunt de paraules atòmiques (sense espais ni signes de puntuació, etc.)
     * @param terms Termes dels que avaluar la rellevància
     * @param num_docs Total de documents del conjunt de documents al que pertany el document
     * @param presence Mapa que guarda en quants documents del sistema apareix un paraula concreta: Map<paraula,cops>
     * @return Index numèric que quantifica la rellevància d'una serie de termes per al contingut del document
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
     * @brief Implementacio de l'algorisme tf booleà
     * @details Calcula com de rellevant es el contingut del document en relació a una serie de termes mitjançant l'algorisme tf booleà
     * @pre El paràmetre terms es un conjunt de paraules atòmiques (sense espais ni signes de puntuació, etc.)
     * @param terms Termes dels que avaluar la rellevància
     * @return Index numèric que quantifica la rellevància d'una sèrie de termes per al contingut del document
     */
    private double termRelevance_tf_boolean(String[] terms) {
        double tf_bool = 0;
        Map<String,Integer> relevantTerms = internalDoc.getRelevantWords(); 
        for (String term : terms) {
            if (relevantTerms.containsKey(term)) tf_bool++;
        }
        return tf_bool;
    }

    //IO
    public String getRepresentation() {
        return "AAAAAAAAAAAAAAAAAAA";
    }

} 
