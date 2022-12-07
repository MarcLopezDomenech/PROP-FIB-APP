package main.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import main.domain.documents.Document;
import main.domain.documents.DocumentsSet;
import main.domain.expressions.ExpressionsSet;
import main.excepcions.*;
import main.domain.expressions.Expression;
import main.domain.util.Pair;
import main.persistence.CtrlPersistence;


/**
 * @class CtrlDomain
 * @brief Controlador de la capa de domini de l'aplicatiu. S'encarrega de rebre peticions de la capa de presentació, aconseguir les dades a partir de les altres classes del domini o de la capa de persistència, i retorna els resultats a la presentació
 * @author pau.duran.manzano
 */
public class CtrlDomain {
    /**
     * \brief Objecte singleton que guarda la única instància del CtrlDomain
     */
    private static CtrlDomain singletonObject;

    /**
     * \brief Instància del conjunt de documents del sistema (DocumentsSet)
     */
    private static DocumentsSet ds;

    /**
     * \brief Instància del conjunt d'expressions del sistema (ExpressionsSet)
     */
    private static ExpressionsSet es;

    /**
     * \brief Instància del controlador de persistència (CtrlPersistence)
     */
    private static CtrlPersistence cp;

    /**
     * @brief Constructora per defecte de CtrlDomain
     * @details Es defineix com a privada perquè CtrlDomain és singleton
     * @pre No existeix cap instància de CtrlDomain ja creada
     * @return CtrlDomain
     */
    private CtrlDomain() {
        ds = DocumentsSet.getInstance();
        es = ExpressionsSet.getInstance();
        cp = CtrlPersistence.getInstance();
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlDomain
     * @details Com que CtrlDomain és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlDomain: Retorna la instància de CtrlDomain
     */
    public static synchronized CtrlDomain getInstance() {
        if (singletonObject == null) singletonObject = new CtrlDomain();
        return singletonObject;
    }

    /**
     * @brief Mètode per donar d'alta un nou document
     * @details L'operació rep el nom i el títol del nou document a donar d'alta a l'aplicatiu, i sempre que no n'existeixi un amb els mateixos identificadors, així ho fa amb contingut buit.
     * @param title Títol del nou document
     * @param author Autor del nou document
     * @param language Idioma del nou document
     * @post Existeix a l'aplicatiu un document identificat per (title, author). Si no existia prèviament, el contingut serà buit.
     * @throws ExceptionDocumentExists en cas que ja existeixi un document identificat per (title, author) a l'aplicatiu
     * @throws ExceptionInvalidLanguage si l'idioma donat no és "ca", "en" ni "es".
     */
    public void createEmptyDocument(String title, String author, String language) throws ExceptionDocumentExists, ExceptionInvalidLanguage {
        ds.createDocument(title, author, "", language);
    }

    /**
     * @brief Operació per donar de baixa un document
     * @details La funció s'encarrega d'esborrar el document de l'aplicatiu identificat pels paràmetres donats.
     * @pre Existeix un document a l'aplicatiu identificat pels paràmetres donats.
     * @param title Títol del document a esborrar
     * @param author Autor del document a esborrar
     * @post No existeix cap document a l'aplicatiu identificat per (title, author)
     * @throws ExceptionNoDocument quan no hi ha cap document identificat per (title, author) donat d'alta
     */
    public void deleteDocument(String title, String author) throws ExceptionNoDocument {
        ds.deleteDocument(title, author);
    }

    /**
     * @brief Funció per saber si existeix un document concret
     * @details La funció retorna un booleà per indicar si el document identificat pels paràmetres donats existeix o no
     * @param title Títol del document que es vol saber si existeix
     * @param author Autor del document que es vol saber si existeix
     * @return Booleà que indica cert si existeix el document (title, author) i fals altrament
     * @post L'estat del sistema no queda alterat
     */
    public Boolean existsDocument(String title, String author) {
        return ds.existsDocument(title, author);
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
        ds.updateTitleAndAuthorDocument(oldTitle, oldAuthor, newTitle, newAuthor);
    }

    /**
     * @brief Funció per obtenir el contingut d'un document
     * @details Amb aquesta operació es pot aconseguir el contingut d'un document, els paràmetres del qual s'han de donar com a paràmetres
     * @pre El document identificat pels paràmetres existeix
     * @param title Títol del document al qual se li vol consultar el contingut
     * @param author Autor del document al qual se li vol consultar el contingut
     * @return String amb el contingut del document (title, author)
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument en el cas que no existeix un document identificat pels paràmetres donats
     */
    public String getContentDocument(String title, String author) throws ExceptionNoDocument {
        return ds.getContentDocument(title, author);
    }

    /**
     * @brief Funció per actualitzar el contingut d'un document
     * @details Aquesta operació permet substituir el contingut d'un document de l'aplcatiu per un de nou
     * @pre El document identificat amb els paràmetres existeix en l'aplicatiu
     * @param title Títol del document a modificar
     * @param author Autor del document a modificar
     * @param content Nou contingut a assignar al document
     * @post El document identificat per (title, author) té com a contingut content
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     */
    public void updateContentDocument(String title, String author, String content) throws ExceptionNoDocument {
        ds.updateContentDocument(title, author, content);
    }

    /**
     * @brief Operació per conseguir l'idioma d'un document
     * @details Retorna l'idioma del document identificat pels paràmetres
     * @pre El document identificat per (title, author) existeix
     * @param title títol del document del que volem l'idioma
     * @param author autor del document del que volem l'idioma
     * @return Es retorna l'idioma del document amb title i author
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument en el cas que no existeix un document identificat pels paràmetres donats
     */
    public String getLanguageDocument(String title, String author) throws ExceptionNoDocument {
       return ds.getLanguageDocument(title, author);
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
     * @throws ExceptionInvalidLanguage quan l'idioma donat no és vàlid ("ca", "en" o "es")
     */
    public void updateLanguageDocument(String title, String author, String newLanguage) throws ExceptionNoDocument, ExceptionInvalidLanguage {
        ds.updateLanguageDocument(title, author, newLanguage);
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
        return ds.isFavouriteDocument(title, author);
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
        ds.updateFavouriteDocument(title, author, favourite);
    }

    /**
     * @brief Mètode per importar documents al sistema
     * @details A partir d'un path absolut, es carrega el document al sistema, assignant-li l'idioma donat
     * @pre El path donat té extensió .txt, .xml o .fp
     * @pre En el text pla, com a mínim la primera línia contindrà el autor, la segona el títol i a partir de la tercera hi serà el contingut. En xml, hi haurà com a mínim etiquetes títol, autor i contingut.
     * @pre L'idioma del paràmetre és "ca", "en" o "es"
     * @pre No existeix un document amb el títol i autor que identifiquen el document que es vol importar
     * @param path Ruta al document que es vol donar afegir al sistema
     * @param language Idioma que se li vol assignar al document a importar
     * @post El sistema té un nou document donat d'alta, que és el que hi havia en el path donat i té com a idioma el donat
     * @throws ExceptionInvalidFormat en cas que el path no tingui extensió .txt, .xml o .fp
     * @throws FileNotFoundException si no es pot accedir a la path donada, per permisos o perquè no existeix
     * @throws ExceptionDocumentExists quan el sistema ja té donat d'alta un document amb els títol i autor del document que es vol importar
     * @throws ExceptionInvalidLanguage en cas que l'idioma del paràmetre no sigui ni "ca" ni "en" ni "es"
     */
    public Object[] importDocument(String path, String language) throws ExceptionInvalidFormat, FileNotFoundException, ExceptionDocumentExists, ExceptionInvalidLanguage {
        String newDoc = cp.importDocument(path, language);
        return ds.importDocument(newDoc);
    }

    /**
     * @brief S'exporta un document a una path
     * @details Funció que permet exportar un document del sistema al disc local de l'usuari
     * @param title Títol del document que es vol exportar
     * @param author Autor del document que es vol exportar
     * @param path Direcció absoluta on es vol exportar el document prèviament identificat
     * @post El sistema no queda alterat, però en el path donat es troba el document del sistema identificat pels paràmetres donats
     * @throws ExceptionNoDocument Quan no existeix al sistema un document identificat per (títol, autor)
     * @throws ExceptionInvalidFormat En cas que el path donat no tingui extensió .txt, .xml o .fp
     * @throws IOException Si no s'ha pogut escriure en el path donat
     */
    public void exportDocument(String title, String author, String path) throws ExceptionNoDocument, ExceptionInvalidFormat, IOException {
        String docRepresentation = ds.getDocumentRepresentation(title, author);
        cp.exportDocument(docRepresentation, path);
    }

    /**
     * @brief Funció per obtenir tots els identificadors dels documents del sistema
     * @details Aquesta funció permet consultar tots els documents que hi ha guardats en el sistema
     * @return Llistat de (favorit, títol, autor) de tots els documents de l'aplicatiu
     * @post L'estat del sistema no queda alterat
     */
    public List<Object[]> listAllDocuments() {
        return ds.listAll();
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
    public List<Object[]> listSimilars(String title, String author, int k, String strategy) throws ExceptionNoDocument, ExceptionInvalidK, ExceptionInvalidStrategy {
        return ds.listSimilars(title, author, k, strategy);
    }

    /**
     * @brief Funció per aconseguir els títols d'un autor
     * @details La funció obté els identificadors (títol i autor) i si és fsvorit dels documents que tenen com a autor el que es dona com a paràmetre
     * @param author Autor a qui se li volen obtenir els títols
     * @return Llista de (favorit, títol, autor) dels documents que tenen com a autor l'autor donat
     * @post L'estat del sistema no queda alterat
     */
    public List<Object[]> listTitlesOfAuthor(String author) {
        return ds.listTitlesOfAuthor(author);
    }

    /**
     * @brief Operació per aconseguir els autors que comencen per un cert prefix
     * @details Aquesta funció serveix per obtenir un llistat amb tots els autors que comencen per un cert prefix
     * @param prefix Prefix que es vol que tinguin els autors
     * @return Llista amb els autors (string) que comencen pel prefix donat
     * @post L'estat del sistema no queda alterat
     */
    public List<String> listAuthorsByPrefix(String prefix) {
        return ds.listAuthorsByPrefix(prefix);
    }

    /**
     * @brief Operació per obtenir identificadors dels documents rellevants a partir d'una query
     * @details Es permet aconseguir els identificadors i la propietat de favorit dels k documents més rellevants en quant a contingut d'una certa query
     * @param query Seguit de paraules a partir de les quals es volen obtenir els documents rellevants
     * @param k Nombre de documents màxims que es vol obtenir
     * @return Llista de (favorit, títol, autor) dels k documents més rellevants, pel que fa a contingut, de la query
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionInvalidK Si la k no és major o igual a 0
     */
    public List<Object[]> listByQuery(String query, int  k) throws ExceptionInvalidK {
        return ds.listByQuery(query, k);
    }


    /**
     * @brief Funció per aconseguir els identificadors dels documents que compleixen una expressió booleana
     * @details Donada una expressió booleana, aquesta funció permet obtenir els identificadors i la propietat de favorit dels documents que contenen alguna frase que la compleixen
     * @pre La cadena de caràcters donada com a paràmetre identifica una expressió vàlida i ja registrada en el sistema
     * @param expression Expressió booleana a partir de la qual es vol realitzar la consulta
     * @param caseSensitive Boole+a que identifica com s'evalua el contigut en l'expressió
     * @return Llista de (favorit, títol, autor) dels documents que tenen alguna frase que compleix l'expressió donada
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoExpression en cas que l'expressió identificada per (expression) no estigui donada d'alta a l'aplicatiu
     */
    public List<Object[]> listByExpression(String expression, Boolean caseSensitive) throws ExceptionNoExpression {
        Expression expr = es.getExpression(expression);
        return ds.listByExpression(expr, caseSensitive);
    }

    /**
     * @brief Mètode per donar d'alta una expressió booleana
     * @details La funció permet donar d'alta una expressió booleana que s'identificarà amb la cadena de caràcters donada
     * @param expression Cadena de caràcters que identificarà la nova expressió booleana
     * @post En cas que la cadena de caràcters donada pugui ser una expressió vàlida i no existeixi prèviament, es dona d'alta una expressió booleana identificada amb aquesta cadena
     * @throws ExceptionExpressionExists si l'string donnat ja identifica una expressió donada d'alta al sistema
     * @throws ExceptionInvalidExpression en cas que l'string donat no sigui apte per constituir una expressió
     */
    public void createExpression(String expression) throws ExceptionExpressionExists, ExceptionInvalidExpression {
        es.createExpression(expression);
    }

    /**
     * @brief Funció per donar de baixa una expressió booleana
     * @details Donada una cadena de caràcters que identifica una expressió booleana del sistema, aquesta es dona de bixa de l'aplicatiu
     * @pre Existeix una expressió identificada per la cadena de caràcters donada
     * @param expression Identificador de l'expressió booleana que es vol esborrar
     * @post Deixa d'estar registrada en l'aplicatiu l'expressió booleana identificada pel paràmetre
     * @throws ExceptionNoExpression en cas que no existeixi cap expressió booleana identificada per (expression)
     */
    public void deleteExpression(String expression) throws ExceptionNoExpression {
        es.deleteExpression(expression);
    }

    /**
     * @brief Operació que permet modificar una expressió booleana
     * @details Amb aquesta funció es permet substituir l'identificador d'una expressió booleana per un de nou
     * @pre Existeix una expressió booleana que té com a identificador oldExpression
     * @param oldExpression Identificador de l'expressió booleana a modificar
     * @param newExpression Nou identificador que es vol donar a l'expressió booleana
     * @post En cas que no existeixi cap expressió booleana identificada per newExpression, es modifica l'identificador de l'expressió identificada per oldExpression pel nou
     * @throws ExceptionNoExpression si no existeix cap expressió identificada per (oldExpression)
     * @throws ExceptionExpressionExists en cas que el nou identificador que es vol donar ja és d'una altra expressió
     * @throws ExceptionInvalidExpression si la nova expressió no té un format vàlid
     */
    public void modifyExpression(String oldExpression, String newExpression) throws ExceptionNoExpression, ExceptionExpressionExists, ExceptionInvalidExpression {
        deleteExpression(oldExpression);
        createExpression(newExpression);
    }

    /**
     * @brief Funció per aconseguir totes les expressions del sistema
     * @details Amb aquesta funció es permet obtenir tots els identificadors de les expressions que hi ha donades d'alta al sistema
     * @return Conjunt de tots els identificadors d'expressions
     * @post L'estat del sistema no queda alterat
     */
    public Set<String> getAllExpressions() {
        return es.getAllExpressions();
    }

    /**
     * @brief Operació per salvar l'estat del sistema
     * @details Aquesta funció dona la possibilitat de guardar tots els documents i expressions donats d'alta al sistema, per poder ser recuperats posteriorment
     * @post El sistema no queda alterat, però tota la informació del sistema queda recollida en un fitxer .fp en el sistema local de l'usuari
     * @throws IOException Si no s'ha pogut escriure en el sistema local la còpia de seguretat creada
     */
    public void saveSystem() throws IOException {
        Set<String> documents = ds.getAllDocumentRepresentations();
        Set<String> expressions = es.getAllExpressions();
        cp.saveSystem(documents, expressions);
    }

    /**
     * @brief Mètode per restaurar el sistema a partir d'una còpia de seguretat
     * @details Aquesta funció permet restaurar l'estat del sistema a partir d'un back up prèviament realitzat del sistema
     * @post El sistema es restaura a partir de la còpia de seguretat que es disposa
     * @throws FileNotFoundException En cas que no es pugui trobar la còpia de seguretat del sistema
     * @throws ExceptionDocumentExists Si algun dels documents de la còpia de seguretat és repetit
     * @throws ExceptionInvalidExpression Si alguna de les expressions de la còpia de seguretat no és correcta
     * @throws ExceptionExpressionExists Si alguna de les expressions de la còpia de seguretat és repetida
     */
    public void restoreSystem() throws FileNotFoundException, ExceptionDocumentExists, ExceptionInvalidExpression, ExceptionExpressionExists {
        Pair<Set<String>,Set<String>> system = cp.restoreSystem();
        Set<String> documents = system.getFirst();
        for (String doc : documents) ds.importDocument(doc);

        Set<String> expressions = system.getSecond();
        for (String expr : expressions) es.createExpression(expr);
    }

    /**
     * @brief Operació per tal de resetejar el sistema
     * @details Amb aquest mètode podem esborrar tota la informació del sistema, deixant-lo buit
     * @post El sistema queda buit, és a dir, deixa de tenir documents i expressions donades d'alta
     */
    public void resetSystem() {
        ds.reset();
        es.reset();
    }

}
