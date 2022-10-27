package main.domain.expressions;

import java.util.Map;
import java.util.HashMap;

import main.domain.expressions.Expression;

/**
 * @class ExpressionsSet
 * @brief Classe que emmagatzema totes les expression booleanes que estan registrades al sistema i que en permet la seva gestió
 * @author pau.duran.manzano
 */
public class ExpressionsSet {
    private static ExpressionsSet singletonObject;

    // Atribut que emmagatzema totes les expressions que hi ha donades d'alta al sistema, mapejades amb el seu identificador
    private Map<String, Expression> expressions;

    /**
     * @brief Constructora per defecte de ExpressionsSet
     * @details Es defineix com a privada perquè ExpressionsSet és singleton
     * @pre No existeix cap instància de ExpressionsSet ja creada
     * @return ExpressionsSet
     */
    private ExpressionsSet() {
        expressions = new HashMap<>();
    }

    /**
     * @brief Funció per aconseguir la instància de ExpressionsSet
     * @details Com que ExpressionsSet és singleton, cal aquesta funció per retornar la seva única instància
     * @return ExpressionsSet: Retorna la instància de ExpressionsSet
     */
    public static synchronized ExpressionsSet getInstance() {
        if (singletonObject == null) {
            singletonObject = new ExpressionsSet();
        }
        return singletonObject;
    }

    /**
     * @brief Funció per aconseguir una expressió
     * @details Aquesta operació permet aconseguir una expressió a partir del seu identificador
     * @pre L'identificador d'expressió donat identifica una expressió ja donada d'alta en el sistema
     * @param id_expression Identificador de l'expressió que es vol obtenir
     * @return Expression identificada per id_expression
     * @post L'estat del sistema no queda alterat
     */
    public Expression getExpression(String id_expression) {     // ToDo: throws exception
        Expression expression = expressions.get(id_expression);
        if (expression == null) return null; // ToDo: throws exception
        return expression;
    }

    /**
     * @brief Mètode per donar d'alta una expressió
     * @details Aquesta funció permet donar d'alta una expressió a partir del seu identificador
     * @param id_expression Identificador que es vol assignar a la nova expressió
     * @post Si no exisitia cap expressió identificada amb l'identificador donat, es dona d'alta una expressió que s'identificarà amb ell.
     */
    public void createExpression(String id_expression) {        // ToDo: throws exception
        Expression newExpression = new Expression(id_expression);
        Expression oldExpression = expressions.put(id_expression, newExpression);

        // Map.put() retorna el value que estava associada a la key donada o null si aquesta key no estava mapejada
        if (oldExpression != null) {
            expressions.put(id_expression, oldExpression);
            // ToDo: throws exception
        }
    }

    /**
     * @brief Mètode per donar de baixa una expressió
     * @details El mètode permet donar de baixa de l'aplicatiu una expressió
     * @pre Existeix una expressió registrada en el sistema identificada amb el paràmetre donat
     * @param id_expression Identificador de l'expressió que es vol esborrar de l'aplicatiu
     * @post El sistema deixa de tenir registrada l'expressió identificada pel paràmetre donat
     */
    public void deleteExpression(String id_expression) {        // ToDo: throws exception
        Expression oldExpression = expressions.remove(id_expression);

        // Map.remove() retorna el value que estava associada a la key donada o null si aquesta key no estava mapejada
        if (oldExpression == null);     // ToDo: throws exception
    }
}