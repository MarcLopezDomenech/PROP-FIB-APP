package main.domain.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import test.domain.expressions.Expression;
import test.domain.documents.Document;
import main.domain.util.Pair;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionNoDocument;

/**
 * @class DocumentsSet
 * @brief Classe encarregada de la gestió de tots els documents de l'aplicatiu.
 * @author marc.lopez.domenech i pau.duran.manzano
 */
public class DocumentsSet {
    private static DocumentsSet singletonObject;

    // Nombre de documents que estan donats d'alta al sistema
    private int numDocuments;

    // Conjunt de documents en format (autor(títol, document))
    private Map<String, Map<String, Document>> documents;

    // Conjunt de paraules que apareixen en algun dels documents anteriors, amb el nombre de documents en què apareix
    // Invariant: Totes les paraules que estan a presence estan a mínim un document (el seu value > 0).
    private Map<String, Integer> presence;

    /**
     * @brief Constructora per defecte de DocumentsSet
     * @details Es defineix com a privada perquè DocumentsSet és singleton
     * @pre No existeix cap instància de DocumentsSet ja creada
     * @return DocumentsSet
     */
    private DocumentsSet() {
        numDocuments = 0;
        documents = new HashMap<>();
        presence = new HashMap<>();
    }

    /**
     * @brief Funció per aconseguir la instància de DocumentsSet
     * @details Com que DocumentsSet és singleton, cal aquesta funció per retornar la seva única instància
     * @return DocumentsSet: Retorna la instància de DocumentsSet
     */
    public static synchronized DocumentsSet getInstance() {
        if (singletonObject == null) {
            singletonObject = new DocumentsSet();
        }
        return singletonObject;
    }

    /**
     * @brief Funció per obtenir el conjunt de documents de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @return Map de (autor, (títol, Document)) de tots els documents guardats a la classe
     */
    public Map<String, Map<String, Document>> getDocuments() {return documents;}

    /**
     * @brief Funció per assignar el conjunt de documents de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @param documents map de (autor, (títol, Document)) que es vol assignar a la classe
     */
    public void setDocuments(Map<String, Map<String, Document>> documents) {
        this.documents = documents;
        numDocuments = documents.size();
        for (Map<String, Document> titlesAuthor : documents.values()) {
            for (Document d : titlesAuthor.values()) {
                addPresence(d.getRelevantWords());
            }
        }
    }

    /**
     * @brief Funció per obtenir l'atribut de presència de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @return Map de valor i quantitat d'aparicions de tots els documents guardats a la classe
     */
    public Map<String, Integer> getPresence() {return presence;}

    /**
     * @brief Operació per crear i registrar un nou document
     * @details Es crea un nou document a l'aplicatiu i es queda registrat al sistema
     * @pre El document identificat per (title, author) no existeix
     * @param title títol que es vol pel nou document
     * @param author autor associat al nou document
     * @param content contingut del nou document
     * @post Es crea el document, es guarda i s'actualizen els atributs interns de la classe
     * @throws ExceptionDocumentExists en cas que ja existeixi un document identificat per (title, author) a l'aplicatiu
     */
    public void createDocument(String title, String author, String content) throws ExceptionDocumentExists {
        if (existsDocument(title, author)) throw new ExceptionDocumentExists(title, author);
        Document newDoc = new Document(title, author, content);
        Map<String, Document> docTitlesAuthor = documents.get(author);

        // Si l'autor no tenia cap títol registrat creem el seu map de títol-document
        if (docTitlesAuthor == null) docTitlesAuthor = new HashMap<>();

        // En qualsevol cas, afegim el títol-document a l'autor i el posem a tots els documents
        docTitlesAuthor.put(title, newDoc);
        documents.put(author, docTitlesAuthor);
        ++numDocuments;

        // Només queda actualitzar el vector de presència
        Set<String> newWords = newDoc.getRelevantWords();
        addPresence(newWords);
    }

    /**
     * @brief Aquest mètode permet esborrar un document de l'aplicatiu
     * @details S'esborra el document que està associat al títol i autor proporcionats
     * @pre Existeix un document identificat per (title, author)
     * @param title títol del document a esborrar
     * @param author autor del document a esborrar
     * @post El document (title, author) queda esborrat de l'aplicatiu i les estructures internes actualitzades adientment
     * @throws ExceptionNoDocument quan no hi ha cap document identificat per (title, author) donat d'alta
     */
    public void deleteDocument(String title, String author) throws ExceptionNoDocument {
        Map<String, Document> docTitlesAuthor = documents.get(author);
        if (docTitlesAuthor == null) throw new ExceptionNoDocument(title, author);
        Document doc = docTitlesAuthor.get(title);
        if (doc == null) throw new ExceptionNoDocument(title, author);
        docTitlesAuthor.remove(title);
        documents.put(author, docTitlesAuthor);
        --numDocuments;

        // Només queda actualitzar el vector de presència
        Set<String> oldWords = doc.getRelevantWords();
        removePresence(oldWords);
    }
    /**
     * @brief Operació per saber la existencia del document
     * @details Retorna un boolea de la existencia del doument identificat pels paràmetres
     * @param title títol del document
     * @param author autor del document
     * @post Es retorna si existeix o no el document definit per title i author
     */
    public Boolean existsDocument(String title, String author) {
        Map<String,Document> maptitle = documents.get(author);
        if (maptitle == null) return false;
        else return maptitle.containsKey(title);
    }
    /**
     * @brief Operació per conseguir el contingut d'un document
     * @details Retorna el contingut del document identificat pels paràmetres
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document del que volem el contingut
     * @param author autor del document del que volem el contingut
     * @post Es retorna el contingut del document amb title i author
     * @throws ExceptionNoDocument en el cas que no existeix un document identificat pels paràmetres donats
     */
    public String getContentDocument(String title, String author) throws ExceptionNoDocument
    {
        Document resdoc = getDocument(title, author);
        return resdoc.getContent();
    }

    /**
     * @brief Operació per actualitzar el contingut d'un document
     * @details El contingut del document identificat pels paràmetres passa a ser el donat per paràmetre
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document a modificar
     * @param author autor del document a modificar
     * @param newContent nou contingut del document
     * @post El document (title, author) té com a contingut newContent
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     */
    public void updateContentDocument(String title, String author, String newContent) throws ExceptionNoDocument {
        Document doc = getDocument(title, author);
        Set<String> oldWords = doc.getRelevantWords();
        removePresence(oldWords);
        doc.setContent(newContent);
        Set<String> newWords = doc.getRelevantWords();
        addPresence(newWords);
    }

    /**
     * @brief Funció per obtenir tots els identificadors dels documents del sistema
     * @details Aquesta funció permet consultar tots els documents que hi ha guardats en el sistema
     * @return Llistat de parells de tots els identificadors de documents de l'aplicatiu
     * @post L'estat del sistema no queda alterat
     */
    public List<Pair<String, String>> listAll() {
        List<Pair<String, String>> all = new ArrayList<Pair<String, String>>();
        for (Map.Entry<String, Map<String, Document>> titlesOfAuthor : documents.entrySet()) {
            String author = titlesOfAuthor.getKey();
            Map<String, Document> titleDoc = titlesOfAuthor.getValue();
            for (Map.Entry<String, Document> documentsOfAuthor : titleDoc.entrySet()) {
                String title = documentsOfAuthor.getKey();
                all.add(new Pair(title, author));
            }
        }
        return all;
    }

    /**
     * @brief Funció per obtenir els identificadors dels k documents més similars a un document
     * @details Amb aquesta operació es poden consultar els documents més similars a un document. En concret, a partir de l'identificador (títol i autor) d'un document, s'obtenen els identificadors dels k documents que són més similars a aquest.
     * @pre El document identificat pels paràmetres donats està donat d'alta a l'aplicatiu
     * @param title Títol del document a què se li vol buscar els similars
     * @param author Autor del document a què se li vol buscar els similars
     * @param k Nombre d'identificadors de documents similars que es vol obtenir
     * @return Llista amb parells dels identificadors (títol, autor) dels com a molt k documents més similars al document donat
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     */
    public List<Pair<String, String>> listSimilars(String title, String author, int k) throws ExceptionNoDocument {
        Document original = getDocument(title, author);
        List<Pair<Pair<String, String>, Double>> ordre = new ArrayList<>();
        for (Map.Entry<String, Map<String, Document>> authorTitleDoc : documents.entrySet()) {
            String aut = authorTitleDoc.getKey();
            Map<String, Document> titleDocs = authorTitleDoc.getValue();
            for (Map.Entry<String, Document> titleDoc : titleDocs.entrySet()) {
                String tit = titleDoc.getKey();
                // En cas que no estiguem en el document original
                if (!(tit.equals(title) && aut.equals(author))) {
                    Document doc = titleDoc.getValue();
                    // Obtenim el valor i l'afegim a la llista
                    Double value = original.compare(doc, numDocuments, presence);
                    ordre.add(new Pair<>(new Pair<>(tit, aut), value));
                }
            }
        }
        // Ordenem la llista en funció dels valors
        Collections.sort(ordre, new ValueComparator());
        List<Pair<String, String>> result = new ArrayList<>();
        Iterator<Pair<Pair<String, String>, Double>> iterator = ordre.listIterator();
        int i = 0;
        // Iterem per tota la llista ordenada fins que arribem a k o al final, per guardar el resultat que retornem
        while(i < k && iterator.hasNext()) {
            result.add(iterator.next().getFirst());
            ++i;
        }
        return result;
    }
    /**
     * @brief Operació per conseguir els documents que compleixen una expressió
     * @details Retorna una llista dels documents identificats per títul i autor que compleixen l'expressió booleana
     * @pre L'expressió expression existeix
     * @param expression Expression en la que volem evaluar els documents
     * @param caseSensitive Boolea que identifica com s'evalua el contigut en l'expressió
     * @post Tots els docuemnts de la llista existeixen i compleixen l'expressió booleana en el cas de caseSensitive
     */
    public List<Pair<String, String>> listByExpression(Expression expression, Boolean caseSensitive) {
        List<Pair<String, String>> expr_list= new ArrayList<Pair<String, String>>();
        for (Map.Entry<String, Map<String, Document>> d : documents.entrySet()) {
            String aut = d.getKey();
            Map<String, Document> titDoc = d.getValue();
            for (Map.Entry<String, Document> d2 : titDoc.entrySet()) {
                 String tit = d2.getKey();
                 Document doc = d2.getValue();
                 if(expression.evaluate(doc.getContent(),caseSensitive)){
                    expr_list.add(new Pair<String,String>(tit,aut));
                 }
            }
        }
        return expr_list;
    }
    /**
     * @brief Operació per conseguir els títols dels documents d'un autor
     * @details Retorna una llista dels documents identificats per títul i autor que ha escrit l'autor
     * @param author autor dels documents que busquem
     * @post Tots els docuemnts de la llista son del autor author
     */
    public List<Pair<String, String>> listTitlesOfAuthor(String author) {
        List<Pair<String, String>> expr_list= new ArrayList<Pair<String,String>>();
        Map<String,Document> maptitle = documents.get(author);
        if (maptitle != null) {
            for (Map.Entry<String, Document> d2 : maptitle.entrySet()) {
                String tit = d2.getKey();
                expr_list.add(new Pair<String, String>(tit, author));
            }
        }
        return expr_list;
    }

    public List<String> listAuthorsByPrefix(String author) {
        ArrayList<String> lista = new ArrayList<String>(); 
        int len = author.length();
        for (Map.Entry<String, Map<String,Document>> entry : documents.entrySet()) {
            String nom=entry.getKey();
            boolean no_err=false;
            for (int i = 0; i < len; i++){
                if (nom.charAt(i) != author.charAt(i)){
                    no_err=false;
                    break;
                }
            }
            if(no_err) lista.add(nom);
        }
        return lista;
    }

    public List<Pair<String, String>> listByQuery(String query, int k) {
        return null;
    }

    private Document getDocument(String title, String author) throws ExceptionNoDocument {
        Map<String,Document> maptitle = documents.get(author);
        if (maptitle == null) throw new ExceptionNoDocument(title, author);
        Document resdoc = maptitle.get(title);
        if (resdoc == null) throw new ExceptionNoDocument(title, author);
        return resdoc;
    }

    /**
     * @brief Mètode per registrar noves presència de paraules a presence
     * @details Per cada paraula donada, s'incrementa el nombre de presència associat, o s'assigna 1 si no estava registrada
     * @pre Les paraules a newPresence corresponen a un contingut que no ha estat prèviament afegit
     * @param newPresence Conjunt de paraules rellevants a registrar
     * @post L'atribut presence queda correctament actualitzat amb la nova presència de paraules
     */
    private void addPresence(Set<String> newPresence) {
        for (String newp : newPresence) {
            Integer num = presence.get(newp);
            if (num == null) presence.put(newp, 1);
            else presence.put(newp, num + 1);
        }
    }

    /**
     * @brief Operació per esborrar la presència en un document de paraules
     * @details Per cada paraula donada, se'n decrementa la seva presència, i si era l'últim registre, s'esborra
     * @pre Totes les paraules a oldPresence apareixen a presence
     * @pre Tots els values de l'atribut presence són com a mínim 1
     * @param oldPresence Conjunt de paraules rellevants a decrementar
     * @post L'atribut presence queda acorrectament actualitzat amb el decrement de la presència de les paraules donades
     * @note This is a stupid note
     */
    private void removePresence(Set<String> oldPresence) {
        for (String oldp : oldPresence) {
            Integer num = presence.get(oldp);
            if (num == 1) presence.remove(oldp);
            else presence.put(oldp, num - 1);
            // El cas num <= 0 no és possible
        }
    }

    /**
     * @class ValueComparator
     * @brief Classe que permet comparar Pair<Pair<String, String>, Double>
     * @author pau.duran.manzano
     */
    private class ValueComparator implements Comparator<Pair<Pair<String, String>, Double>> {
        /**
         * @brief Operació per comparar en funció del segon valor (el second) d'un pair
         * @pre Cap dels valors dels camps de dins dels pairs és null
         * @param p1 Primer pair a comparar
         * @param p2 Segon pair a comprar
         * @return Es retorna 1 si p1 té un second major que p2, 0 si són iguals i -1 altrament
         */
        @Override
        public int compare(Pair<Pair<String, String>, Double> p1, Pair<Pair<String, String>, Double> p2) {
            if (p1.getSecond() > p2.getSecond()) return 1;
            else if (p1.getSecond() == p2.getSecond()) return 0;
            else return -1;
        }
    }
}
