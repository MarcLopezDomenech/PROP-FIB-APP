package main.presentation;

import main.domain.CtrlDomain;
import main.domain.util.Pair;
import main.excepcions.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @class CtrlPresentation
 * @brief Controlador de la capa de presentació de l'aplicatiu. S'encarrega de la part gràfica de l'aplicatiu, és a dir, de la gestió de les vistes. Alhora, però, requereix de la comunicació amb el controlador de domini per resoldre peticions i mostrar informació.
 * @author pau.duran.manzano
 */
public class CtrlPresentation {
    /**
     * \brief Objecte singleton que guarda la única instància del CtrlDomain
     */
    private static CtrlPresentation singletonObject;

    /**
     * \brief Instància del controlador de domini (CtrlDomain)
     */
    private static CtrlDomain cd;

    /**
     * @brief Constructora per defecte de CtrlPresentation
     * @details Es defineix com a privada perquè CtrlPresentation és singleton
     * @pre No existeix cap instància de CtrlPresentation ja creada
     * @return CtrlPresentation
     */
    private CtrlPresentation() {
        cd = CtrlDomain.getInstance();
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlPresentation
     * @details Com que CtrlPresentation és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlPresentation: Retorna la instància de CtrlPresentation
     */
    public static CtrlPresentation getInstance() {
        if (singletonObject == null) singletonObject = new CtrlPresentation();
        return singletonObject;
    }


    // Gestió de l'aplicació i back-ups

    /**
     * @brief Funció que inicialitza l'aplicatiu amb la còpia de seguretat
     * @details Amb aquest mètode es permet inicialitzar l'aplicatiu a partir de l'última còpia de seguretat de què es disposi
     * @pre Si existeix una còpia de seguretat, aquestà és vàlida
     * @post El sistema queda inicialitzat amb la darrera còpia de seguretat del sistema
     */
    public void initiateApp() {
        // Restaurar sistema
        try {
            cd.restoreSystem();
        } catch (FileNotFoundException | ExceptionDocumentExists | ExceptionInvalidExpression | ExceptionExpressionExists e) {
            // No hauría de passar mai
            JFrame reference = new JFrame();
            reference.setLocation(new Point(600, 300));
            showInternalError(reference);
        }
    }

    /**
     * @brief Funció que deixa l'estat del sistema en una còpia de seguretat
     * @details Aquest mètode s'ha de cridar abans de tancar l'aplicatiu per tal de salvar-ne el sistema localment i poder-lo restablir en la següent posada en marxa
     * @param reference Frame de referència per saber on posicionar el dialog en cas d'error
     * @post El sistema no queda alterat, però es genera una còpia de seguretat a partir de l'estat actual, que queda emmagatzemada
     */
    public void closeApp(JFrame reference) {
        try {
            cd.saveSystem();
        } catch (IOException e) {
            // Tenim un problema greu
            showInternalError(reference);
        }
    }

    /**
     * @brief Operació per tal de resetejar el sistema
     * @details Amb aquest mètode podem esborrar tota la informació del sistema, deixant-lo buit
     * @post El sistema queda buit, és a dir, deixa de tenir documents i expressions donades d'alta
     */
    public void reset() {
        cd.resetSystem();
    }


    // Dialogs d'error, confirmació i ajuda

    /**
     * @brief Mètode que mostra un missatge d'error
     * @details A partir d'un missatge d'error donat, amb aquest mètode mostrem un diàleg d'error per pantalla
     * @param reference Frame de referència per saber on posicionar el diàleg d'error
     * @param message Missatge d'error que es vol mostrar per pantalla
     * @post Es mostra per pantalla un diàleg d'error
     */
    public void showError(JFrame reference, String message) {
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.initialize(reference, message);
    }

    /**
     * @brief Mètode per mostrar el missatge d'error intern
     * @details En cas que es produeixi un error intern a l'aplicatiu, aquest mètode permet mostrar l'avís a l'usuari
     * @param reference Frame de referència per saber on posicionar el diàleg d'error
     * @post Es mostra per pantalla el diàleg d'error intern
     */
    public void showInternalError(JFrame reference) {
        //Suggerencia: "No hem detectat cap copia de seguretat del sistema, prem OK per iniciar l'aplicació"
        showError(reference, "Hi ha hagut un error intern al sistema. Si no es el primer cop que succeeix, si us plau contacta amb l'administrador.");
    }

    /**
     * @brief Funció per demanar confirmació
     * @details En cas de voler realitzar una acció destructiva, aquest mètode permet demanar-ne confirmació a l'usuari
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param message Missatge de confirmació que es vol verificar per part de l'usuari
     * @return Es retorna si l'usuari ha confirmat (true) o no (false)
     * @post Es mostra per pantalla el diàleg de confirmació amb el missatge donat
     */
    public boolean askConfirmation(JFrame reference, String message) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        return confirmDialog.initialize(reference, message);
    }

    /**
     * @brief Mètode per mostrar el diàleg d'ajuda
     * @details Per les vistes que ho necessiten, amb aquest mètode es permet mostrar una ajuda a l'usuari
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param message Missatge d'ajuda que es vol mostrar
     * @post Es mostra per pantalla el diàleg d'ajuda amb el missatge donat
     */
    public void showHelp(JFrame reference, String message) {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.initialize(reference, message);
    }



    // Opcions del menú

    /**
     * @brief Funció per carregar documents a l'aplicatiu
     * @details Es permet mostrar el diàleg per carregar documents locals, retornant informació dels documents carregats
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Array d'informació dels documents carregats (si és preferit, títol i autor) o null si no es puja cap
     * @post Es mostra per pantalla el diàleg per carregar documents i s'actualizen els actuals si és el cas
     */
    public Object[][] showLoader(JFrame reference) {
        LoaderDialog dialog = new LoaderDialog();
        Pair<String, String[]> languageAndPaths = dialog.initialize(reference);
        List<Object[]> newData = new ArrayList<>();

        if (languageAndPaths == null) return null;      // No s'ha introduït cap path

        String language = languageAndPaths.getFirst();
        String[] paths = languageAndPaths.getSecond();
        for (String path : paths) {
            try {
                // Importem el document i aconseguim les dades (fav, títol, autor)
                Object[] document = importDocument(path, language);
                newData.add(document);
            } catch (ExceptionInvalidFormat | ExceptionDocumentExists e) {
                // Excepcions nostres que indiquen errors que es poden donar
                showError(reference, e.getMessage());
            } catch (FileNotFoundException e) {
                // Excepció de Java produïda quan la ruta no és correcta
                showError(reference, "La ruta que has especificat no es correcta");
            } catch (ExceptionInvalidLanguage e) {
                // Per presentació garantim que això no pot passar. Si es dona, tenim error intern
                showInternalError(reference);
            }
        }

        return newData.toArray(new Object[0][]);
    }

    /**
     * @brief Funció per crear nous documents a l'aplicatiu
     * @details Es permet mostrar el diàleg per crear un document nou, retornant informació del document creat
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Informació (favorit, títol, autor) del document creat o null si s'ha cancel·lat la creació
     * @post Es mostra per pantalla el diàleg de creació i es retorna, si és el cas, informació del document creat
     */
    public Object[] showNewDocument(JFrame reference) {
        NewDocumentDialog dialog = new NewDocumentDialog();
        Pair<Pair<String, String>, String> titleAuthorLang = dialog.initialize(reference);

        Object[] newDoc = null;
        if (titleAuthorLang != null) {
            try {
                String title = titleAuthorLang.getFirst().getFirst();
                String author = titleAuthorLang.getFirst().getSecond();
                String language = titleAuthorLang.getSecond();
                createEmptyDocument(title, author, language);
                newDoc = new Object[]{false, title, author};      // Per defecte, no és favorit
            } catch (ExceptionDocumentExists e) {
                showError(reference, e.getMessage());
            } catch (ExceptionInvalidLanguage e) {
                // NO hauria de passar
                showInternalError(reference);
            }
        }

        return newDoc;      // Serà null si no s'ha donat d'alta cap document al final
    }

    /**
     * @brief Mètode per mostrar la vista de la gestió de documents
     * @details Es dimensiona i situa la vista de gestió de documents (vista principal) i es mostra
     * @param location Posició de la pantalla on es vol situar la vista
     * @param size Tamany de la vista
     * @post Es mostra per pantalla la vista de gestió de documents
     */
    public void showDocuments(Point location, Dimension size) {
        MainView mw = new MainView();
        mw.initialize(location, size);
    }

    /**
     * @brief Mètode per mostrar la vista de la gestió d'expressions
     * @details Es dimensiona i situa la vista de gestió d'expressions i es mostra
     * @param location Posició de la pantalla on es vol situar la vista
     * @param size Tamany de la vista
     * @post Es mostra per pantalla la vista de gestió d'expressions
     */
    public void showExpressions(Point location, Dimension size) {
        ExpressionsView ew = new ExpressionsView();
        ew.initialize(location, size);
    }

    // Opcions de llistar de documents

    /**
     * @brief Funció per filtrar documents per query
     * @details Es mostra el diàleg de llistar per query i es retorna el resultat de la cerca
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Llista de (favorit, títol, autor) dels k documents més rellevants, pel que fa a contingut, de la query introduïda al diàleg
     * @post Es mostra el diàleg i, quan s'acaba el cas d'ús, es torna a la vista on estava
     */
    public Object[][] showListByQuery(JFrame reference) {
        Object[][] docs = null;
        ListQueryDialog dialog = new ListQueryDialog();
        Pair<String, Integer> queryAndK = dialog.initialize(reference);
        String query = queryAndK.getFirst();
        Integer k = queryAndK.getSecond();
        try {
            // En cas que s'hagin introduït la query i la k
            if (query != null && k != null) docs = listByQuery(query, k);
        } catch (ExceptionInvalidK e) {
            // Pot passar que la k no sigui vàlida, mostrem error
            showError(reference, e.getMessage());
        }
        return docs;
    }

    /**
     * @brief Funció per filtrar documents per expressió
     * @details Es mostra el diàleg de llistar per expressió i es retorna el resultat de la cerca
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Llista de (favorit, títol, autor) dels documents que compleixen l'expressió introduïda al dàleg
     * @post Es mostra el diàleg i, quan s'acaba el cas d'ús, es torna a la vista on estava
     */
    public Object[][] showListByExpression(JFrame reference) {
        Object[][] docs = null;
        ListExpressionDialog dialog = new ListExpressionDialog();
        Pair<String, Boolean> exprAndSensitive = dialog.initialize(reference);
        String expression = exprAndSensitive.getFirst();
        Boolean caseSensitive = exprAndSensitive.getSecond();
        try {
            // En cas que s'hagi introduït una expressió
            if (expression != null) docs = listByExpression(expression, caseSensitive);
        } catch (ExceptionNoExpression e) {
            // En cas d'introduir una expressió que no tenim donada d'alta
            showError(reference, e.getMessage());
        }
        return docs;
    }

    /**
     * @brief Funció per filtrar documents per autor
     * @details Es mostra el diàleg de llistar per autor i es retorna el resultat de la cerca
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Llista de (favorit, títol, autor) dels documents de l'autor introduït al diàleg
     * @post Es mostra el diàleg i, quan s'acaba el cas d'ús, es torna a la vista on estava
     */
    public Object[][] showListByAuthor(JFrame reference) {
        Object[][] docs = null;
        ListAuthorDialog dialog = new ListAuthorDialog();
        String author = dialog.initialize(reference);
        // En cas que s'hagi introduït un autor
        if (author != null) docs = listTitlesOfAuthor(author);
        return docs;
    }


    // Opcions amb document seleccionat

    /**
     * @brief Mètode per mostrar el diàleg de modificar un document
     * @pre Existeix a l'aplicatiu un document identificat pel títol i autor donats.
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param title Títol del document a modificar
     * @param author Autor del document a modificar
     * @return Un parell amb títol i autor en cas que s'hagi modificat algun d'aquests valors, null altrament
     * @post Es mostra el diàleg per modificar el document i quan s'acaba el cas d'ús es retorna a la vista on estava
     */
    public Pair<String, String> showModify(JFrame reference, String title, String author) {
        ModifyDialog md = new ModifyDialog();
        return md.initialize(reference, title, author);
    }

    /**
     * @brief Mètode per mostrar el diàleg de llistar similars
     * @pre Existeix a l'aplicatiu un document identificat pel títol i autor donats.
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param title Títol del document a modificar
     * @param author Autor del document a modificar
     * @return Llista de (favorit, títol, autor) dels k documents similars al seleccionat
     * @post Es mostra el diàleg per introduir els valors necessaris i quan s'acaba el cas d'ús es retorna a la vista on estava
     */
    public Object[][] showListKSimilars(JFrame reference, String title, String author){
        ListKSimilarsDialog dialog = new ListKSimilarsDialog();
        Pair<Integer, String> kAndStrategy = dialog.initialize(reference, title, author);
        Object[][] docs = null;
        try {
            if (kAndStrategy != null) {
                Integer k = kAndStrategy.getFirst();
                String strategy = kAndStrategy.getSecond();
                docs = listSimilars(title, author, k, strategy);
            }
        } catch (ExceptionInvalidK e) {
            // Pot passar que la k sigui invàlida --> mostrem error
            showError(reference, e.getMessage());
        } catch (ExceptionNoDocument | ExceptionInvalidStrategy e) {
            // Per presentació garantim que el document existeix i l'estratègia és vàlida --> si no, tenim error intern
            showInternalError(reference);
        }
        return docs;
    }

    /**
     * @brief Mètode per mostrar el diàleg d'exportar un document
     * @pre Existeix a l'aplicatiu un document identificat pel títol i autor donats.
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param title Títol del document a modificar
     * @param author Autor del document a modificar
     * @post Es mostra el diàleg per exportar el document i quan s'acaba el cas d'ús es retorna a la vista on estava
     */
    public void showDownloader(JFrame reference, String title, String author){
        DownloaderDialog dialog = new DownloaderDialog();
        String path = dialog.initialize(reference, title, author);

        if (path != null){
            try {
                exportDocument(title, author, path);
            } catch (ExceptionInvalidFormat | ExceptionNoDocument | IOException e) {
                // No hauria de passar, ho garantim per presentació
                showInternalError(reference);
            }
        }
    }

    // Crides al domini

    /**
     * @brief Mètode per donar d'alta un nou document
     * @details L'operació rep el nom i el títol del nou document a donar d'alta a l'aplicatiu, i sempre que no n'existeixi un amb els mateixos identificadors, així ho fa amb contingut buit.
     * @param title Títol del nou document
     * @param author Autor del nou document
     * @param language Idioma del nou document
     * @post Existeix a l'aplicatiu un document identificat per (title, author). Si no existia prèviament, el contingut serà buit.
     * @throws ExceptionDocumentExists en cas que ja existeixi un document identificat per (title, author) a l'aplicatiu
     * @throws ExceptionInvalidLanguage en cas que l'idioma introduït no sigui "ca", "en" ni "es"
     */
    public void createEmptyDocument(String title, String author, String language) throws ExceptionDocumentExists, ExceptionInvalidLanguage {
        cd.createEmptyDocument(title, author, language);
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
        cd.deleteDocument(title, author);
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
        return cd.existsDocument(title, author);
    }

    /**
     * @brief Mètode per actualitzar el títol i l'autor d'un document
     * @details Aquesta funció ens permet modificar l'identificador d'un document, és a dir, el seu títol i el seu autor
     * @pre Existeix un document identificat pels paràmetres (oldTitle, oldAuthor)
     * @param oldTitle Títol del document que volem modificar
     * @param oldAuthor Autor del document que volem modificar
     * @param newTitle Nou títol que li volem donar al document
     * @param newAuthor Nou autor que li volem donar al document
     * @post En cas que s'hagi modificat l'identificador del document donat i no hi hagués cap altre document identificat per (newTitle, newAuthor), es modifica l'identificador del document (oldTitle, oldAuthor) per aquesta nova parella d'identificadors. Altrament, es llença una excepció
     * @throws ExceptionNoDocument En cas que no existeixi al sistema un document identificat per (oldTitle, oldAuthor)
     * @throws ExceptionDocumentExists En cas que ja existeixi al sistema un document identificat per (newTitle, newAuthor)
     */
    public void updateTitleAndAuthorDocument(String oldTitle, String oldAuthor, String newTitle, String newAuthor) throws ExceptionNoDocument, ExceptionDocumentExists {
        cd.updateTitleAndAuthorDocument(oldTitle, oldAuthor, newTitle, newAuthor);
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
        return cd.getContentDocument(title, author);
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
        cd.updateContentDocument(title, author, content);
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
        return cd.getLanguageDocument(title, author);
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
        cd.updateLanguageDocument(title, author, newLanguage);
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
        return cd.isFavouriteDocument(title, author);
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
        cd.updateFavouriteDocument(title, author, favourite);
    }

    /**
     * @brief Funció per obtenir tots els identificadors dels documents del sistema
     * @details Aquesta funció permet consultar tots els documents que hi ha guardats en el sistema
     * @return Llistat de (favorit, títol, autor) de tots els documents de l'aplicatiu
     * @post L'estat del sistema no queda alterat
     */
    public Object[][] listAllDocuments() {
        List<Object[]> result = cd.listAllDocuments();
        return result.toArray(new Object[0][]);
    }

    /**
     * @brief Funció per obtenir els identificadors dels k documents més similars a un document
     * @details Amb aquesta operació es poden consultar els documents més similars a un document. En concret, a partir de l'identificador (títol i autor) d'un document, s'obtenen els identificadors dels k documents que són més similars a aquest.
     * @pre El document identificat pels paràmetres donats està donat d'alta a l'aplicatiu
     * @param title Títol del document a què se li vol buscar els similars
     * @param author Autor del document a què se li vol buscar els similars
     * @param k Nombre d'identificadors de documents similars que es vol obtenir
     * @param strategy Estratègia que es vol emprar per buscar similars
     * @return Llista de (favorit, títol, autor) dels com a molt k documents més similars al document donat amb l'estratègia triada
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoDocument quan no existeix un document identificat per (title, author)
     * @throws ExceptionInvalidK quan la k donada no és major o igual a 0
     * @throws ExceptionInvalidStrategy en cas que l'estratègia no sigui una de les opcions (tf-idf o tf-boolean)
     */
    public Object[][] listSimilars(String title, String author, int k, String strategy) throws ExceptionNoDocument, ExceptionInvalidK, ExceptionInvalidStrategy {
        List<Object[]> result = cd.listSimilars(title, author, k, strategy);
        return result.toArray(new Object[0][]);
    }

    /**
     * @brief Funció per aconseguir els títols d'un autor
     * @details La funció obté els identificadors (títol i autor) dels documents que tenen com a autor el que es dona com a paràmetre
     * @param author Autor a qui se li volen obtenir els títols
     * @return Llista de (favorit, títol, autor) dels documents que tenen com a autor l'autor donat
     * @post L'estat del sistema no queda alterat
     */
    public Object[][] listTitlesOfAuthor(String author) {
        List<Object[]> result = cd.listTitlesOfAuthor(author);
        return result.toArray(new Object[0][]);
    }

    /**
     * @brief Operació per aconseguir els autors que comencen per un cert prefix
     * @details Aquesta funció serveix per obtenir un llistat amb tots els autors que comencen per un cert prefix
     * @param prefix Prefix que es vol que tinguin els autors
     * @return Llista amb els autors (string) que comencen pel prefix donat
     * @post L'estat del sistema no queda alterat
     */
    public List<String> listAuthorsByPrefix(String prefix) {
        return cd.listAuthorsByPrefix(prefix);
    }

    /**
     * @brief Operació per obtenir identificadors dels documents rellevants a partir d'una query
     * @details Es permet aconseguir els identificadors dels k documents més rellevants en quant a contingut d'una certa query
     * @param query Seguit de paraules a partir de les quals es volen obtenir els documents rellevants
     * @param k Nombre de documents màxims que es vol obtenir
     * @return Llista amb (favorit, títol, autor) dels k documents més rellevants, pel que fa a contingut, de la query
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionInvalidK Si la k no és major o igual a 0
     */
    public Object[][] listByQuery(String query, int  k) throws ExceptionInvalidK {
        List<Object[]> result = cd.listByQuery(query, k);
        return result.toArray(new Object[0][]);
    }

    /**
     * @brief Funció per aconseguir els identificadors dels documents que compleixen una expressió booleana
     * @details Donada una expressió booleana, aquesta funció permet obtenir els identificadors dels documents que contenen alguna frase que la compleixen
     * @pre La cadena de caràcters donada com a paràmetre identifica una expressió vàlida i ja registrada en el sistema
     * @param expression Expressió booleana a partir de la qual es vol realitzar la consulta
     * @param caseSensitive Per indicar si volem o no una consulta case sensitive
     * @return Llista de (favorit, títol, autor) dels documents que tenen alguna frase que compleix l'expressió donada
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoExpression en cas que l'expressió identificada per (expression) no estigui donada d'alta a l'aplicatiu
     */
    public Object[][] listByExpression(String expression, Boolean caseSensitive) throws ExceptionNoExpression {
        List<Object[]> result = cd.listByExpression(expression, caseSensitive);
        return result.toArray(new Object[0][]);
    }

    /**
     * @brief Mètode per donar d'alta una expressió booleana
     * @details La funció permet donar d'alta una expressió booleana que s'identificarà amb la cadena de caràcters donada
     * @param expression Cadena de caràcters que identificarà la nova expressió booleana
     * @post En cas que la cadena de caràcters donada pugui ser una expressió vàlida i no existeixi prèviament, es dona d'alta una expressió booleana identificada amb aquesta cadena
     * @throws ExceptionExpressionExists si l'string donat ja identifica una expressió donada d'alta al sistema
     * @throws ExceptionInvalidExpression en cas que l'string donat no sigui apte per constituir una expressió
     */
    public void createExpression(String expression) throws ExceptionExpressionExists, ExceptionInvalidExpression {
        cd.createExpression(expression);
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
        cd.deleteExpression(expression);
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
        cd.modifyExpression(oldExpression, newExpression);
    }

    /**
     * @brief Funció per aconseguir totes les expressions del sistema
     * @details Amb aquesta funció es permet obtenir tots els identificadors de les expressions que hi ha donades d'alta al sistema
     * @return Conjunt de tots els identificadors d'expressions
     * @post L'estat del sistema no queda alterat
     */
    public Set<String> getAllExpressions() {
        return cd.getAllExpressions();
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
     * @return Objecte amb (favorit, títol, autor) del documet importat
     * @post El sistema té un nou document donat d'alta, que és el que hi havia en el path donat i té com a idioma el donat
     * @throws ExceptionInvalidFormat en cas que el path no tingui extensió .txt, .xml o .fp
     * @throws FileNotFoundException si no es pot accedir a la path donada, per permisos o perquè no existeix
     * @throws ExceptionDocumentExists quan el sistema ja té donat d'alta un document amb els títol i autor del document que es vol importar
     * @throws ExceptionInvalidLanguage en cas que l'idioma del paràmetre no sigui ni "ca" ni "en" ni "es"
     */
    public Object[] importDocument(String path, String language) throws ExceptionInvalidFormat, FileNotFoundException, ExceptionDocumentExists, ExceptionInvalidLanguage {
        return cd.importDocument(path, language);
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
        cd.exportDocument(title, author, path);
    }



    // ToDo: Treure això d'aquí
    public static void main(String[] args) {
        CtrlPresentation cp = CtrlPresentation.getInstance();
        cp.initiateApp();
        cp.showDocuments(new Point(600, 300), new Dimension(500, 500));
    }

}
