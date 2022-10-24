package domain.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.documents.Document;
import domain.expressions.Expression;
import domain.util.Pair;

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
     * @brief Operació per crear i registrar un nou document
     * @details Es crea un nou document a l'aplicatiu i es queda registrat al sistema
     * @pre El document identificat per (title, author) no existeix
     * @param title títol que es vol pel nou document
     * @param author autor associat al nou document
     * @param content contingut del nou document
     * @post Es crea el document, es guarda i s'actualizen els atributs interns de la classe
     */
    public void createDocument(String title, String author, String content) {
        if (existsDocument(title, author)); // ToDo: throws exception
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
     */
    public void deleteDocument(String title, String author) {
        Map<String, Document> docTitlesAuthor = documents.get(author);
        if (docTitlesAuthor == null); // ToDo: throws exception
        Document doc = docTitlesAuthor.get(title);
        if (doc == null); // ToDo: throws exception
        docTitlesAuthor.remove(title);
        documents.put(author, docTitlesAuthor);
        --numDocuments;

        // Només queda actualitzar el vector de presència
        Set<String> oldWords = doc.getRelevantWords();
        removePresence(oldWords);
    }

    public Boolean existsDocument(String title, String author) {
        Map<String,Document> maptitle=documents.get(author);
        return maptitle.containsKey(title);
    }

    public String getContentDocument(String title, String author) throws ExcepNoDoc
    {
        Document resdoc = getDocument(title, author);
        if(resdoc == null){
            //EXCEPTION
            throw new ExcepNoDoc("El documento con título y autor: "+title +' '+author+" no existe");
        }
        else{
            return resdoc.getContent();
        }
    }

    /**
     * @brief Operació per actualitzar el contingut d'un document
     * @details El contingut del document identificat pels paràmetres passa a ser el donat per paràmetre
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document a modificar
     * @param author autor del document a modificar
     * @param newContent nou contingut del document
     * @post El document (title, author) té com a contingut newContent
     */
    public void updateContentDocument(String title, String author, String newContent) {
        Document doc = getDocument(title, author);
        Set<String> oldWords = doc.getRelevantWords();
        removePresence(oldWords);
        doc.setContent(newContent);
        Set<String> newWords = doc.getRelevantWords();
        addPresence(newWords);
    }

    public List<Pair<String, String>> listSimilars(String title, String author, int k) {
        return null;
    }

    public List<Pair<String, String>> listByExpression(Expression expression) {
        List<Pair<String, String>> expr_list;
        for (Map.Entry<String, Map<String, Document>> d : documents.entrySet()) {
            String aut = d.getKey();
            Map<String, Document> titDoc = d.getValue();
            for (Map.Entry<String, Document> d2 : titDoc.entrySet()) {
                 String tit = d2.getKey();
                 Document doc = d2.getValue();
                 if(expression.evaluate(doc.getContent())){
                    expr_list.add(new Pair<String,String>(tit,aut));
                 }
            }
        }
        return expr_list;
    }

    public List<Pair<String, String>> listTitlesOfAuthor(String author) {
        List<Pair<String, String>> expr_list= new ArrayList<Pair<String,String>>();
        Map<String,Document> maptitle = documents.get(author);
        for (Map.Entry<String, Document> d2 : maptitle.entrySet()) {
            String tit = d2.getKey();
            expr_list.add(new Pair<String,String>(tit,author));
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

    private Document getDocument(String title, String author) {
        Map<String,Document> maptitle = documents.get(author);
        Document resdoc = maptitle.get(title);
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

}
