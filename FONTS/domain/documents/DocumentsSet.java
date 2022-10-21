package domain.documents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// ToDo: Trobar el import per poder fer servir Pair

import domain.documents.Document;

/**
 * @class DocumentsSet
 * @brief Classe encarregada de la gestió de tots els documents de l'aplicatiu.
 * @author marc.lopez.domenech i pau.duran.manzano
 */
public class DocumentsSet {
    private static DocumentsSet singletonObject;
    private Map<String, Map<String, Document>> documents;   // Conjunt de documents en format (autor(títol, document))
    private Map<String, Integer> presence;                  // Conjunt de paraules que apareixen en algun dels documents anteriors, amb el nombre de documents en què apareix

    /**
     * @brief Constructora per defecte de DocumentsSet
     * @details Es defineix com a privada perquè DocumentsSet és singleton
     * @pre No existeix cap instància de DocumentsSet ja creada
     * @return DocumentsSet
     */
    private DocumentsSet() {
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

    public void createDocument(String title, String author, String content) {

    }

    public void deleteDocument(String title, String author) {

    }

    public Boolean existsDocument(String title, String author) {
        return null;
    }

    public String getContentDocument(String title, String author) {
        return null;
    }

    public void updateContentDocument(String title, String author, String newContent) {

    }

    public List<Pair<String, String>> listSimilars(String title, String author) {
        return null;
    }

    public List<Pair<String, String>> listByExpression(String expression) {
        return null;
    }

    public List<Pair<String, String>> listTitlesOfAuthor(String author) {
        return null;
    }

    public List<String> listAuthorsByPrefix(String author) {
        return null;
    }

    public List<Pair<String, String>> listByQuery(String query, int k) {
        return null;
    }

    private Document getDocument(String title, String author) {
        return null;
    }

    private void addPresence(Set<String> newPresence) {

    }

    private void removePresence(Set<String> oldPresence) {

    }

}
