package domain;

import java.util.List;
import javafx.util.Pair;

import domain.documents.DocumentsSet;
import domain.expressions.ExpressionsSet;
import domain.expressions.Expression;


/**
 * @class CtrlDomain
 * @brief Controlador de la capa de domini de l'aplicatiu. S'encarrega de rebre peticions de la capa de presentació, aconseguir les dades a partir de les altres classes del domini o de la capa de persistència, i retorna els resultats a la presentació
 * @author pau.duran.manzano
 */
public class CtrlDomain {
    private static CtrlDomain singletonObject;

    private static DocumentsSet ds;
    private static ExpressionsSet es;

    /**
     * @brief Constructora per defecte de CtrlDomain
     * @details Es defineix com a privada perquè CtrlDomain és singleton
     * @pre No existeix cap instància de CtrlDomain ja creada
     * @return CtrlDomain
     */
    private CtrlDomain() {
        ds = DocumentsSet.getInstance();
        es = ExpressionsSet.getInstance();
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

    void createEmptyDocument(String title, String author) { // ToDo: throws exception
        ds.createDocument(title, author, "");
    }

    void deleteDocument(String title, String author) {      // ToDo: throws exception
        ds.deleteDocument(title, author);
    }

    Boolean existsDocument(String title, String author) {
        return ds.existsDocument(title, author);
    }

    String getContentDocument(String title, String author) {  // ToDo: throws exception
        return ds.getContentDocument(title, author);
    }

    void updateContentDocument(String title, String author, String content) {     // ToDo: throws ex
        ds.updateContentDocument(title, author, content);
    }

    List<Pair<String, String>> listSimilars(String title, String author, int k) {
        return ds.listSimilars(title, author, k);
    }

    List<Pair<String, String>> listTitlesOfAuthor(String author) {
        return ds.listTitlesOfAuthor(author);
    }

    List<Pair<String, String>> listAuthorsByPrefix(String author) {
        return ds.listTiltesOfAuthor(author);
    }

    List<Pair<String, String>> listByQuery(String query, int  k) {
        return ds.listByQuery(query, k);
    }

    List<Pair<String, String>> listByExpression(String expression) {
        Expression expr = es.getExpression(expression);
        return ds.listByExpression(expr);
    }

    void createExpression(String expression) {      // ToDo: throws exception
        es.createExpression(expression);
    }

    void deleteExpression(String expression) {    // ToDo: throws exception
        es.deleteExpression(expression);
    }

    void modifyExpression(String oldExpression, String newExpression) {   // ToDo: throws exception
        deleteExpression(oldExpression);
        createExpression(newExpression);
    }

}
