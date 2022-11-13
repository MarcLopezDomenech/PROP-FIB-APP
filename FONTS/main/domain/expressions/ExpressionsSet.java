package main.domain.expressions;

import java.util.Map;
import java.util.HashMap;

import main.domain.expressions.Expression;          // Canviar per test.domain.expressions.Expression per fer tests
import main.excepcions.ExceptionExpressionExists;
import main.excepcions.ExceptionInvalidExpression;
import main.excepcions.ExceptionNoExpression;

/**
 * @class ExpressionsSet
 * @brief Classe que emmagatzema totes les expression booleanes que estan registrades al sistema i que en permet la seva gestió
 * @author pau.duran.manzano
 */
public class ExpressionsSet {
    /**
     * \brief Objecte singleton que guarda la única instància d'ExpressionsSet
     */
    private static ExpressionsSet singletonObject;

    /**
     * \brief Atribut que emmagatzema totes les expressions que hi ha donades d'alta al sistema, mapejades amb el seu identificador
     * \invariant La clau de cada entrada identifica l'expressió que té assignada com a valor
     */
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
     * @brief Funció per obtenir el conjunt d'expressions de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @return Map d'identificador-expressió de totes les expressions guardades a la classe
     */
    public Map<String, Expression> getExpressions() {return expressions;}

    /**
     * @brief Funció per assignar el conjunt d'expressions de la classe
     * @details Aquesta funció és necessària per poder fer tests, tot i que no s'emprarà fora de les proves
     * @param expressions map d'identificador-expressió que es vol assignar a la classe
     */
    public void setExpressions(Map<String, Expression> expressions) {this.expressions = expressions;}

    /**
     * @brief Funció per aconseguir una expressió
     * @details Aquesta operació permet aconseguir una expressió a partir del seu identificador
     * @pre L'identificador d'expressió donat identifica una expressió ja donada d'alta en el sistema
     * @param id_expression Identificador de l'expressió que es vol obtenir
     * @return Expression identificada per id_expression
     * @post L'estat del sistema no queda alterat
     * @throws ExceptionNoExpression si no existeix cap expressió identificada per (id_expression)
     */
    public Expression getExpression(String id_expression) throws ExceptionNoExpression {
        Expression expression = expressions.get(id_expression);
        // El .get retorna null en cas de no trobar l'expressió, per tant si és així cal llençar l'excepció
        if (expression == null) throw new ExceptionNoExpression(id_expression);
        return expression;
    }

    /**
     * @brief Mètode per donar d'alta una expressió
     * @details Aquesta funció permet donar d'alta una expressió a partir del seu identificador
     * @param id_expression Identificador que es vol assignar a la nova expressió
     * @post Si no exisitia cap expressió identificada amb l'identificador donat, es dona d'alta una expressió que s'identificarà amb ell.
     * @throws ExceptionExpressionExists si l'string donnat ja identifica una expressió donada d'alta al sistema
     */
    public void createExpression(String id_expression) throws ExceptionExpressionExists, ExceptionInvalidExpression {
        Expression newExpression = Expression.create(id_expression);
        Expression oldExpression = expressions.put(id_expression, newExpression);

        // Map.put() retorna el value que estava associada a la key donada o null si aquesta key no estava mapejada
        if (oldExpression != null) {
            // En cas que no retorni null, vol dir que ja existia una expressió amb aquell identificador. Llencem excepció i tornem a posar la original
            expressions.put(id_expression, oldExpression);
            throw new ExceptionExpressionExists(id_expression);
        }
    }

    /**
     * @brief Mètode per donar de baixa una expressió
     * @details El mètode permet donar de baixa de l'aplicatiu una expressió
     * @pre Existeix una expressió registrada en el sistema identificada amb el paràmetre donat
     * @param id_expression Identificador de l'expressió que es vol esborrar de l'aplicatiu
     * @post El sistema deixa de tenir registrada l'expressió identificada pel paràmetre donat
     * @throws ExceptionNoExpression en cas que no existeixi cap expressió booleana identificada per (id_expression)
     */
    public void deleteExpression(String id_expression) throws ExceptionNoExpression {
        Expression oldExpression = expressions.remove(id_expression);

        // Map.remove() retorna el value que estava associada a la key donada o null si aquesta key no estava mapejada
        if (oldExpression == null) throw new ExceptionNoExpression(id_expression);
    }
}