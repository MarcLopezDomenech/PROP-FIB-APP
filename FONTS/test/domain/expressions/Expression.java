package test.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

/**
 * @class Expression (stub)
 * @brief Classe stub de Expression
 * @author pau.duran.manzano
 */
public class Expression {
    /**
     * @brief Funció per imitar la creació d'una expressió
     * @details En la classe de veritat, Expression és abstracte i té aquest mètode per donar-ne d'alta una. Aquí el declarem també, amb la possbilitat de llençar l'excepció de invalid (una possibilitat en la classe de veritat) quan rebem el string "invalid", per poder fer proves
     * @param expression amb el string que identificaria l'expressió
     * @return Expressió booleana per poder provar, amb les funcionalitats bàsiques
     * @throws ExceptionInvalidExpression en cas que el string donat digui "invalid"
     */
    public static Expression create(String expression) throws ExceptionInvalidExpression {
        if ("invalid".equals(expression)) throw new ExceptionInvalidExpression("prova");
        return new Expression();
    }

    /**
     * @brief Funció per imitar l'avaluació d'una expressió
     * @details Implementem un mètode per poder fer tests que vulguin avaluar un contingut a partir d'una expressió booleana
     * @param content amb el contingut que es voldria avaluar
     * @param caseSensitive indicant si caldria fer una avaluació case sensitive o no
     * @return Retorna cert si el contingut donat és "true", altrament retorna fals.
     */
    public Boolean evaluate(String content, Boolean caseSensitive) {
        return "true".equals(content);
    }
}
