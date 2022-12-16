package main.presentation;

import main.domain.util.Pair;
import main.excepcions.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @class CtrlViewsDialogs
 * @brief Subcontrolador de la capa de presentació de l'aplicatiu. S'encarrega de la gestió de les vistes i els diàlegs, és a dir, de les seva instanciació i inicialització.
 * @author pau.duran.manzano
 */
public class CtrlViewsDialogs {
    /**
     * \brief Objecte singleton que guarda la única instància del CtrlViewsDialogs
     */
    private static CtrlViewsDialogs singletonObject;

    /**
     * \brief Instància del controlador de presentació (CtrlPresentation)
     */
    private static CtrlPresentation cp;

    /**
     * @brief Constructora per defecte de CtrlViewsDialogs
     * @details Es defineix com a privada perquè CtrlViewsDialogs és singleton
     * @pre No existeix cap instància de CtrlViewsDialogs ja creada
     * @return CtrlViewsDialogs
     */
    private CtrlViewsDialogs() {
        cp = CtrlPresentation.getInstance();
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlViewsDialogs
     * @details Com que CtrlViewsDialogs és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlViewsDialogs: Retorna la instància de CtrlViewsDialogs
     */
    public static CtrlViewsDialogs getInstance() {
        if (singletonObject == null) singletonObject = new CtrlViewsDialogs();
        return singletonObject;
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
        showError(reference, "Hi ha hagut un error intern al sistema. Si no és el primer cop que succeeix, si us plau contacta amb l'administrador.");
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
     * @details Es permet mostrar el diàleg per carregar documents locals
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Booleà que indica si s'ha carregat correctament algun document nou
     * @post Es mostra per pantalla el diàleg per carregar documents i s'actualizen els actuals si és el cas
     */
    public boolean showLoader(JFrame reference) {
        LoaderDialog dialog = new LoaderDialog();
        Pair<String, String[]> languageAndPaths = dialog.initialize(reference);

        if (languageAndPaths == null) return false;      // No s'ha introduït cap path

        String language = languageAndPaths.getFirst();
        String[] paths = languageAndPaths.getSecond();
        for (String path : paths) {
            try {
                // Importem el document i aconseguim les dades (fav, títol, autor)
                cp.importDocument(path, language);
            } catch (ExceptionInvalidFormat | ExceptionDocumentExists | ExceptionInvalidCharacter | ExceptionMissingTitleOrAuthor e) {
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

        return true;
    }

    /**
     * @brief Funció per crear nous documents a l'aplicatiu
     * @details Es permet mostrar el diàleg per crear un document nou
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @return Booleà que indica si s'ha creat realment un document nou
     * @post Es mostra per pantalla el diàleg de creació
     */
    public boolean showNewDocument(JFrame reference) {
        NewDocumentDialog dialog = new NewDocumentDialog();
        Pair<Pair<String, String>, String> titleAuthorLang = dialog.initialize(reference);

        if (titleAuthorLang == null) return false;

        try {
            String title = titleAuthorLang.getFirst().getFirst();
            String author = titleAuthorLang.getFirst().getSecond();
            String language = titleAuthorLang.getSecond();
            cp.createEmptyDocument(title, author, language);
        } catch (ExceptionDocumentExists e) {
            showError(reference, e.getMessage());
        } catch (ExceptionInvalidLanguage e) {
            // NO hauria de passar
            showInternalError(reference);
        }

        return true;
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
     * @brief Mètode per mostrar la vista de la gestió de documents
     * @details Es dimensiona i situa la vista de gestió de documents (vista principal) i es mostra al centre de la pantalla
     * @param size Tamany de la vista
     * @post Es mostra per pantalla la vista de gestió de documents
     */
    public void showDocuments(Dimension size) {
        MainView mw = new MainView();
        mw.initialize(size);
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
            if (query != null && k != null) docs = cp.listByQuery(query, k);
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
            if (expression != null) docs = cp.listByExpression(expression, caseSensitive);
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
        if (author != null) docs = cp.listTitlesOfAuthor(author);
        return docs;
    }


    // Opcions amb document seleccionat

    /**
     * @brief Mètode per mostrar el diàleg de modificar un document
     * @details Aquesta funció serveix per poder modificar un document a partir d'un document vàlid que s'ha seleccionat a la vista principal
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
     * @details Aquesta funció serveix per mostrar documents semblants a un document a partir d'un document vàlid que s'ha seleccionat a la vista principal
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
                docs = cp.listSimilars(title, author, k, strategy);
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
     * @details El mètode ens permet obrir el diàleg d'exportar el document i, efectivament, exportar-lo
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
                cp.exportDocument(title, author, path);
            } catch (ExceptionInvalidFormat | ExceptionNoDocument | IOException e) {
                // No hauria de passar, ho garantim per presentació
                showInternalError(reference);
            }
        }
    }


    // Opcions de gestió d'expressions

    /**
     * @brief Mètode per mostrar el diàleg de modificar una expressió
     * @details Aquesta funció serveix per poder modificar una expressió a partir d'un identificador vàlid d'expressió ja seleccionada
     * @param reference Frame de referència per saber on posicionar el diàleg
     * @param expression Identificador de l'expressió que es vol modificar
     * @return Es retorna el nou identificador que l'usuari vol per l'expressió donada al paràmetre
     * @post Es mostra el diàleg per modificar l'expressió i quan s'acaba el cas d'ùs es retorna a la vista on estava
     */
    public String showModifyExpression(JFrame reference, String expression) {
        ExpressionsModifyDialog modifyExpression = new ExpressionsModifyDialog();
        return modifyExpression.initialize(reference, expression);
    }

}
