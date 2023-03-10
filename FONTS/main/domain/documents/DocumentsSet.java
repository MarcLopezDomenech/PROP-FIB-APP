package main.domain.documents;

import java.io.FileNotFoundException;
import java.util.*;

import main.domain.util.Trie;
import main.domain.expressions.Expression;      // Canviar per test.domain.expressions.Expression per fer tests
import main.domain.documents.Document;          // Canviar per test.domain.documents.Document per fer tests
import main.domain.util.Pair;
import main.excepcions.*;

/**
 * @class DocumentsSet
 * @brief Classe encarregada de la gestió de tots els documents de l'aplicatiu.
 * @author marc.lopez.domenech i pau.duran.manzano
 */
public class DocumentsSet {
    /**
     * \brief Atribut que representa el conjunt de documents del sistema 
    */
    private static DocumentsSet singletonObject;
    
    /**
     * \brief Nombre de documents que estan donats d'alta al sistema
     * \invariant  numDocuments serà sempre >= 0
    */
    private int numDocuments;

    /**
     * \brief Arbre trie dels autors donats d'alta al sistema
     */
    private Trie arb_aut;
    /**
     * \brief Nombre de documents que estan donats d'alta al sistema
     * \invariant un document es única i està identificar per un títul i un autor
    */
    private Map<String, Map<String, Document>> documents;

    /**
     * \brief Conjunt de paraules que apareixen en algun dels documents anteriors, amb el nombre de documents en què apareix
     * \invariant Totes les paraules que estan a presence estan a mínim un document (el seu value > 0).
    */
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
        arb_aut= new Trie();
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

        // A més, hem de contar el nombre de documents i posar les relevant words de cada documents a presence
        numDocuments = 0;
        presence = new HashMap<>();

        for (Map<String, Document> titlesAuthor : documents.values()) {
            for (Document d : titlesAuthor.values()) {
                addPresence(d.getRelevantWords());
                ++numDocuments;
            }
        }
        for (Map.Entry<String, Map<String, Document>> d : documents.entrySet()) {
            arb_aut.insertOnce(d.getKey());
        }
    }

    /**
     * @brief Funció per obtenir el número de documents de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @return int amb el nombre de documents guardats a la classe
     */
    public int getnumDocuments() {return numDocuments;}

    /**
     * @brief Funció per obtenir el mapa de presència de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @return Map de (paraula,vegades que apareix) amb les paraules que apareixen als documents de la classe
     */
    public Map<String, Integer> getpresence() {return presence;}

    /**
     * @brief Operació per crear i registrar un nou document
     * @details Es crea un nou document a l'aplicatiu i es queda registrat al sistema
     * @pre El document identificat per (title, author) no existeix
     * @param title títol que es vol pel nou document
     * @param author autor associat al nou document
     * @param content contingut del nou document
     * @param language idioma del nou document
     * @post Es crea el document, es guarda i s'actualizen els atributs interns de la classe
     * @throws ExceptionDocumentExists en cas que ja existeixi un document identificat per (title, author) a l'aplicatiu
     * @throws ExceptionInvalidLanguage si l'idioma donat no és "ca", "en" ni "es".
     */
    public void createDocument(String title, String author, String content, String language) throws ExceptionDocumentExists, ExceptionInvalidLanguage {
        Document newDoc = new Document(title, author, content, language);
        registerDocument(newDoc);
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

        // Si no existeix l'autor, podem afirmar que no existeix el doc
        if (docTitlesAuthor == null) throw new ExceptionNoDocument(title, author);
        Document doc = docTitlesAuthor.get(title);

        // Si no existeix el títol per aquell autor vol dir que no existeix el doc
        if (doc == null) throw new ExceptionNoDocument(title, author);
        docTitlesAuthor.remove(title);

        // Si l'autor es queda sense títols, el traiem. Si no, l'actualitzem sense el títol esborrat
        if (docTitlesAuthor.isEmpty()) documents.remove(author);
        else documents.put(author, docTitlesAuthor);

        // Hem eliminat un document del sistema
        --numDocuments;

        //Eliminem un dels doc del autor
        arb_aut.removeOnce(author);

        // Només queda actualitzar el vector de presència
        Set<String> oldWords = doc.getRelevantWords();
        //System.out.println("oldWords:" + oldWords);
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
     * @brief Mètode per actualitzar el títol i l'autor d'un document
     * @details Aquesta funció ens permet modificar l'identificador d'un document, és a dir, el seu títol i el seu autor
     * @pre El títol o l'autor nous són diferents als que actualment identifiquen el document
     * @pre Existeix un document identificat per (oldTitle, oldAuthor)
     * @param oldTitle Títol del document que volem modificar
     * @param oldAuthor Autor del document que volem modificar
     * @param newTitle Nou títol que li volem donar al document
     * @param newAuthor Nou autor que li volem donar al document
     * @post En cas que no hi hagués cap altre document identificat per (newTitle, newAuthor), es modifica l'identificador del document (oldTitle, oldAuthor) per aquesta nova parella d'identificadors. Altrament, es llença una excepció
     * @throws ExceptionNoDocument En cas que no existeixi al sistema un document identificat per (oldTitle, oldAuthor)
     * @throws ExceptionDocumentExists En cas que ja existeixi al sistema un document identificat per (newTitle, newAuthor)
     */
    public void updateTitleAndAuthorDocument(String oldTitle, String oldAuthor, String newTitle, String newAuthor) throws ExceptionNoDocument, ExceptionDocumentExists {
        //System.out.println("Update title and author");
        if (!(oldTitle.equals(newTitle)) || !(oldAuthor.equals(newAuthor))) {       // Si no ha canviat ni el títol ni l'autor, no cal fer res
            // Si no podrem afegir el document perquè ja existeix
            if (existsDocument(newTitle, newAuthor)) throw new ExceptionDocumentExists(newTitle, newAuthor);
            // Si podem, realitzem el canvi
            Document newDocument = new Document(getDocument(oldTitle, oldAuthor));
            newDocument.setTitle(newTitle);
            newDocument.setAuthor(newAuthor);
            registerDocument(newDocument);                 // El tornem a afegir amb els nous títol i autor
            deleteDocument(oldTitle, oldAuthor);        // Esborrem el document del conjunt
            //PotencialProblema
        }
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
    public String getContentDocument(String title, String author) throws ExceptionNoDocument {
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
        //System.out.println("Update content");
        Document doc = getDocument(title, author);

        // Esborrem les relevants words de l'antic contingut de presence
        Set<String> oldWords = doc.getRelevantWords();
        //System.out.println("oldWords:" + oldWords);
        removePresence(oldWords);

        // Modifiquem el contingut del document
        doc.setContent(newContent);

        // Posem les relevants words del nou contingut a presence
        Set<String> newWords = doc.getRelevantWords();
        //System.out.println("newWords:" + newWords);
        addPresence(newWords);
    }

    /**
     * @brief Operació per conseguir l'idioma d'un document
     * @details Retorna l'idioma del document identificat pels paràmetres
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document del que volem el contingut
     * @param author autor del document del que volem el contingut
     * @post Es retorna l'idioma del document amb title i author
     * @throws ExceptionNoDocument en el cas que no existeix un document identificat pels paràmetres donats
     */
    public String getLanguageDocument(String title, String author) throws ExceptionNoDocument {
        Document resdoc = getDocument(title, author);
        return resdoc.getLanguage();
    }

    /**
     * @brief Operació per actualitzar l'idioma d'un document
     * @details L'idioma del document identificat pels paràmetres passa a ser el donat per paràmetre
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document a modificar
     * @param author autor del document a modificar
     * @param newLanguage nou idioma del document
     * @post El document (title, author) té com a idioma newLanguage
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     * @throws ExceptionInvalidLanguage quan l'idioma donat no és vàlid (ca, en o es)
     */
    public void updateLanguageDocument(String title, String author, String newLanguage) throws ExceptionNoDocument, ExceptionInvalidLanguage {
        Document doc = getDocument(title, author);

        Set<String> oldWords = doc.getRelevantWords();
        //System.out.println("oldWords:" + oldWords);
        removePresence(oldWords);

        // Modifiquem el llenguatge del document
        doc.setLanguage(newLanguage);

        // Posem les relevants words del nou contingut a presence
        Set<String> newWords = doc.getRelevantWords();
        //System.out.println("newWords:" + newWords);
        addPresence(newWords);
    }

    /**
     * @brief Operació per saber si un document és favorit o no
     * @details Retorna si el document identificat pels paràmetres està marcat com a preferit o no
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document del que volem saber si és favorit
     * @param author autor del document del que volem saber si és favorit
     * @return Cert o fals en funció de si el document és o no és, respectivament, favorit
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument en el cas que no existeix un document identificat pels paràmetres donats
     */
    public boolean isFavouriteDocument(String title, String author) throws ExceptionNoDocument {
        Document document = getDocument(title, author);
        return document.isFavourite();
    }

    /**
     * @brief Operació per actualitzar la propietat de favorit
     * @details El document identificat pels paràmetres serà o no favorit en funció del paràmetre rebut
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document a modificar
     * @param author autor del document a modificar
     * @param favourite si es vol el document com a favorit o no
     * @post El document (title, author) és o no favorit en funció del paràmetre (favourite)
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     */
    public void updateFavouriteDocument(String title, String author, boolean favourite) throws ExceptionNoDocument {
        Document document = getDocument(title, author);
        document.setFavourite(favourite);
    }

    /**
     * @brief Funció per obtenir tots els identificadors dels documents del sistema
     * @details Aquesta funció permet consultar tots els documents que hi ha guardats en el sistema
     * @return Llistat de (favorit, títol, autor) de tots els documents de l'aplicatiu
     * @post L'estat del sistema no queda alterat
     */
    public List<Object[]> listAll() {
        List<Object[]> all = new ArrayList<Object[]>();
        for (Map.Entry<String, Map<String, Document>> titlesOfAuthor : documents.entrySet()) {
            String author = titlesOfAuthor.getKey();
            Map<String, Document> titleDoc = titlesOfAuthor.getValue();
            for (Map.Entry<String, Document> documentsOfAuthor : titleDoc.entrySet()) {
                String title = documentsOfAuthor.getKey();
                Document document = documentsOfAuthor.getValue();
                boolean favourite = document.isFavourite();
                all.add(new Object[]{favourite, title, author});
            }
        }
        return all;
    }

    /**
     * @brief Funció per obtenir els identificadors dels k documents més similars a un document
     * @details Amb aquesta operació es poden consultar els documents més similars a un document. En concret, a partir de l'identificador (títol i autor) d'un document, s'obtenen els identificadors i si són favorits dels k documents que són més similars a aquest.
     * @pre El document identificat pels paràmetres donats està donat d'alta a l'aplicatiu
     * @param title Títol del document a què se li vol buscar els similars
     * @param author Autor del document a què se li vol buscar els similars
     * @param k Nombre d'identificadors de documents similars que es vol obtenir
     * @param strategy Estratègia que es vol emprar per buscar similars
     * @return Llista de (favorit, títol, autor) dels com a molt k documents més similars al document donat
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     * @throws ExceptionInvalidK quan la k donada no és major o igual a 0
     * @throws ExceptionInvalidStrategy en cas que l'estratègia no sigui una de les opcions (tf-idf o tf-boolean)
     */
    public List<Object[]> listSimilars(String title, String author, int k, String strategy) throws ExceptionNoDocument, ExceptionInvalidStrategy, ExceptionInvalidK {
        // Comprovem que l'estratègia és vàlida
        if (!"tf-idf".equals(strategy) && !"tf-boolean".equals(strategy)) throw new ExceptionInvalidStrategy(strategy);

        // Comprovem que el valor de k és vàlid
        if (k < 0) throw new ExceptionInvalidK(k);

        Document original = getDocument(title, author);
        List<Pair<Object[], Double>> ordre = new ArrayList<>();
        for (Map.Entry<String, Map<String, Document>> authorTitleDoc : documents.entrySet()) {
            String aut = authorTitleDoc.getKey();
            Map<String, Document> titleDocs = authorTitleDoc.getValue();
            for (Map.Entry<String, Document> titleDoc : titleDocs.entrySet()) {
                String tit = titleDoc.getKey();
                // En cas que no estiguem en el document original
                if (!(tit.equals(title) && aut.equals(author))) {
                    Document doc = titleDoc.getValue();
                    boolean favourite = doc.isFavourite();
                    // Obtenim el valor (en funció de l'estratègia triada) i l'afegim a la llista
                    Double value;
                    if ("tf-idf".equals(strategy)) value = original.compare_tf_idf(doc, numDocuments, presence);
                    else value = original.compare_tf_boolean(doc);
                    
                    ordre=adding(ordre,new Object[]{favourite, tit, aut},value,k);
                }
            }
        }
        List<Object[]> result = new ArrayList<Object[]>();
        Iterator<Pair<Object[], Double>> iterator = ordre.listIterator();
        while(iterator.hasNext()){
            result.add(0,iterator.next().getFirst());
        }
        return result;

    }

    /**
     * @brief Operació per conseguir els títols dels documents d'un autor
     * @details Retorna una llista dels documents identificats per títul i autor que ha escrit l'autor
     * @param author autor dels documents que busquem
     * @return Llista de (favorit, títol, autor) dels documents que tenen com a autor l'autor donat
     * @post Tots els docuemnts de la llista son del autor author
     */
    public List<Object[]> listTitlesOfAuthor(String author) {
        List<Object[]> expr_list= new ArrayList<Object[]>();
        Map<String,Document> maptitle = documents.get(author);
        if (maptitle != null) {
            for (Map.Entry<String, Document> d2 : maptitle.entrySet()) {
                String tit = d2.getKey();
                Document doc = d2.getValue();
                boolean favourite = doc.isFavourite();
                expr_list.add(new Object[]{favourite, tit, author});
            }
        }
        return expr_list;
    }

    /**
     * @brief Operació per aconseguir els autors que el seu nom comença per un prefix donat
     * @details Retorna una llista dels autors que el seu nom comença pel prefix que rep la funció
     * @param prefix prefix del author
     * @return Llista amb els autors (string) que comencen pel prefix donat
     * @post Tots els autors que comencen pel prefix author
     */
    public List<String> listAuthorsByPrefix(String prefix) {
        ArrayList<String> result = new ArrayList<String>();
        result = arb_aut.wordsGivenPrefix(prefix);
        return result;
    }

    /**
     * @brief Operació per conseguir una llista dels k documents que compleixen millor la query
     * @details Retorna una llista de de titles i authors que identifiquen a documents que compleixen la query
     * @param query que volem aplicar als documents
     * @param k nombre de documents que volem retornar
     * @return Llista de (favorit, títol, autor) dels k documents més rellevants, pel que fa a contingut, de la query
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionInvalidK Si la k no és major o igual a 0
     */
    public List<Object[]> listByQuery(String query, int k) throws ExceptionInvalidK {
        if (k < 0) throw new ExceptionInvalidK(k);
        List<Pair<Object[], Double>> ordre = new ArrayList<>();
        for (Map.Entry<String, Map<String, Document>> authorTitleDoc : documents.entrySet()) {
            String author = authorTitleDoc.getKey();
            Map<String, Document> titleDocs = authorTitleDoc.getValue();
            for (Map.Entry<String, Document> titleDoc : titleDocs.entrySet()) {
                String title = titleDoc.getKey();
                Document doc = titleDoc.getValue();
                boolean favourite = doc.isFavourite();
                Double value = doc.queryRelevance(query, numDocuments, presence);
                ordre=adding(ordre,new Object[]{favourite, title, author},value,k);
            }
        }
        List<Object[]> result = new ArrayList<Object[]>();
        Iterator<Pair<Object[], Double>> iterator = ordre.listIterator();
        while(iterator.hasNext()){
            result.add(0,iterator.next().getFirst());
        }
        return result;
    }

    /**
     * @brief Operació per conseguir els documents que compleixen una expressió
     * @details Retorna una llista dels documents identificats per títol i autor que compleixen l'expressió booleana
     * @pre L'expressió expression existeix
     * @param expression Expression en la que volem evaluar els documents
     * @param caseSensitive Booleà que identifica com s'evalua el contigut en l'expressió
     * @return Llista de (favorit, títol, autor) dels documents que tenen alguna frase que compleix l'expressió donada
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoExpression en cas que l'expressió identificada per (expression) no estigui donada d'alta a l'aplicatiu
     */
    public List<Object[]> listByExpression(Expression expression, Boolean caseSensitive) {
        List<Object[]> expr_list= new ArrayList<Object[]>();
        for (Map.Entry<String, Map<String, Document>> d : documents.entrySet()) {
            String aut = d.getKey();
            Map<String, Document> titDoc = d.getValue();
            for (Map.Entry<String, Document> d2 : titDoc.entrySet()) {
                String tit = d2.getKey();
                Document doc = d2.getValue();
                boolean favourite = doc.isFavourite();
                // En cas que el contingut del document compleixi l'expressió, l'afegim al resultat
                if(expression.evaluate(doc.getContent() + " " + doc.getTitle(), caseSensitive)){
                    expr_list.add(new Object[]{favourite, tit, aut});
                }
            }
        }
        return expr_list;
    }

    /**
     * @brief Funció per importar un document al conjunt
     * @details A partir del format propietari d'un document, s'importa al sistema
     * @pre L'string donat compleix el format propietari definit al sistema
     * @param document Document en format propietari a crear
     * @return Informació bàsica del document importat (favorit, títol, autor)
     * @post Es dona d'alta al sistema el document importat
     * @throws ExceptionDocumentExists En cas que ja existeixi un document identificat per títol i autor del document importat
     */
    public Object[] importDocument(String document) throws ExceptionDocumentExists {
        // Creem el document i aconseguim la informació bàsica
        Document newDoc = new Document(document);
        boolean favorite = newDoc.isFavourite();
        String title = newDoc.getTitle();
        String author = newDoc.getAuthor();
        // Registrem el document al conjunt
        registerDocument(newDoc);
        // Retornem la informació bàsica
        return new Object[]{favorite, title, author};
    }

    /**
     * @brief Funció per aconseguir la representació en format propietari d'un document
     * @details A partir d'aquesta funció podem obtenir el document en format propietari
     * @pre Existeix un document a l'aplicatiu identificat pels paràmetres donats
     * @param title Títol del document de què volem aconseguir la seva representació
     * @param author Autor del document de què volem aconseguir la seva representació
     * @return Representació en format propietari del document identificat
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument Si no existeix a l'aplicatiu un document identificat pels paràmetres donats
     */
    public String getDocumentRepresentation(String title, String author) throws ExceptionNoDocument {
        Document document = getDocument(title, author);
        return document.getRepresentation();
    }

    /**
     * @brief Funció per aconseguir totes les representacions dels documents
     * @details Amb aquesta funció podem obtenir totes les representacions en format propietari dels documents que tenim al conjunt
     * @return Conjunt de representacions de tots els documents del conjunt
     * @post L'estat del sistema no queda alterat
     */
    public Set<String> getAllDocumentRepresentations() {
        Set<String> representations = new HashSet<String>();
        for (Map.Entry<String, Map<String, Document>> d : documents.entrySet()) {
            Map<String, Document> titlesDoc = d.getValue();
            for (Map.Entry<String, Document> d2 : titlesDoc.entrySet()) {
                Document document = d2.getValue();
                String representation = document.getRepresentation();
                representations.add(representation);
            }
        }
        return representations;
    }

    /**
     * @brief Mètode per resetejar el conjunt
     * @details En cas de voler esborrar tots els documents emmagatzemats, es pot emprar aquesta funció
     * @return Es retorna el nou conjunt de documents, que ara és buit
     * @post El conjunt queda com si s'hagués encés l'aplicatiu per primera vegada
     */
    public DocumentsSet reset() {
        singletonObject = new DocumentsSet();
        return singletonObject;
    }

    /**
     * @brief Operació per conseguir el document que te un títol i autor deteminat
     * @details Retorna un document identificat per l'author igual a uthor i títol igual a title
     * @param author author del document
     * @param title title del document
     * @post El document identificat per aquell títol i aquell author
     */
    private Document getDocument(String title, String author) throws ExceptionNoDocument {
        Map<String,Document> maptitle = documents.get(author);
        if (maptitle == null) throw new ExceptionNoDocument(title, author);
        Document resdoc = maptitle.get(title);
        if (resdoc == null) throw new ExceptionNoDocument(title, author);
        return resdoc;
    }

    /**
     * @brief Mètode per registrar un nou document
     * @details Es permet afegir un document al conjunt adequadament
     * @param newDoc Document a afegir al conjunt
     * @post El document donat queda registrat al sistema
     * @throws ExceptionDocumentExists En cas que el document donat ja existeixi al conjunt
     */
    private void registerDocument(Document newDoc) throws ExceptionDocumentExists {
        String title = newDoc.getTitle();
        String author = newDoc.getAuthor();

        if (existsDocument(title, author)) throw new ExceptionDocumentExists(title, author);
        Map<String, Document> docTitlesAuthor = documents.get(author);

        // Si l'autor no tenia cap títol registrat creem el seu map de títol-document
        if (docTitlesAuthor == null) docTitlesAuthor = new HashMap<>();

        // En qualsevol cas, afegim el títol-document a l'autor i el posem a tots els documents
        docTitlesAuthor.put(title, newDoc);
        documents.put(author, docTitlesAuthor);

        // Ara tenim un document més
        ++numDocuments;

        //Afegim l'autor
        arb_aut.insertOnce(author);

        // Només queda actualitzar el vector de presència
        Set<String> newWords = newDoc.getRelevantWords();
        //System.out.println("newWords:" + newWords);
        addPresence(newWords);
    }

    /**
     * @brief Mètode per registrar noves presència de paraules a presence
     * @details Per cada paraula donada, s'incrementa el nombre de presència associat, o s'assigna 1 si no estava registrada
     * @pre Les paraules a newPresence corresponen a un contingut que no ha estat prèviament afegit
     * @param newPresence Conjunt de paraules rellevants a registrar
     * @post L'atribut presence queda correctament actualitzat amb la nova presència de paraules
     */
    private void addPresence(Set<String> newPresence) {
        //System.out.println("Procedim a add:" + newPresence);
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
     */
    private void removePresence(Set<String> oldPresence) {
        //System.out.println("procedim a remove:" + oldPresence);

        for (String oldp : oldPresence) {
            Integer num = presence.get(oldp);
            if (num == 1) presence.remove(oldp);
            else presence.put(oldp, num - 1);
            // El cas num <= 0 no és possible
        }
    }

    /**
     * @brief Operació per ordenar un conjunt de documents segons un pes (value)
     * @pre El valor del double no és null
     * @param ordre Llista de documents amb valors ordenats pel double
     * @param ext Conjunt de paràmetres del document que no sigui el valor associat
     * @param value Valor associat al pes del document
     * @param k Nombre de documents que volem a la llista
     * @return Retorna una llista ordenada ascendentment de pairs on el primer valor és la informació del document i el segon el valor pel que està ordenat
     */
    private List<Pair<Object[], Double>> adding(List<Pair<Object[], Double>> ordre, Object[] ext,Double value,int k){
        int tam=ordre.size();
        if(tam==0){
            //System.out.println("Zero");
            ordre.add(0,new Pair<>(ext, value));
            return ordre;
        }
        int ini=0;
        int fin=tam-1;
        boolean added=true;
        while(added){
            if(ini>fin){
                ordre.add(ini,new Pair<>(ext, value));
                added=false;
            }
            else {
                int med = (ini + fin) / 2;
                if (ordre.get(med).getSecond() == value) {
                    ordre.add(med, new Pair<>(ext, value));
                    added=false;
                } else if (ordre.get(med).getSecond() > value) {
                    fin = med - 1;
                } else {
                    ini = med + 1;
                }
            }
        }
        if(tam>=k){
            ordre=ordre.subList(1,k+1);
        }
        return ordre;
    }
}
