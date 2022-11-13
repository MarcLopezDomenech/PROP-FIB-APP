package test.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

/**
 * @class Expression (stub)
 * @brief Classe stub de Expression
 * @author pau.duran.manzano
 */
public class Expression {
    /**
     * @brief Constructora per defecte
     * @return Instància de Expression
     */
    public Expression() {
    }

    /**
     * @brief Funció per imitar la creació d'una expressió
     * @details En la classe de veritat, Expression és abstracte i té aquest mètode per donar-ne d'alta una. Aquí el declarem també, amb la possbilitat de llençar l'excepció de invalid (una possibilitat en la classe de veritat) quan rebem el string "invalid", per poder fer proves
     * @param expression amb el string que identificaria l'expressió
     * @return Expressió booleana per poder provar, amb les funcionalitats bàsiques. En cas que el paràmetre sigui "true", es retorna una instància de True. En cas que el paràmetre sigui "false", una de False. Altrament, una Expression de cap de les dues subclasses.
     * @throws ExceptionInvalidExpression en cas que el string donat digui "invalid"
     */
    public static Expression create(String expression) throws ExceptionInvalidExpression {
        if ("invalid".equals(expression)) throw new ExceptionInvalidExpression("prova");
        else if ("true".equals(expression)) return new True();
        else if ("false".equals(expression)) return new False();
        return new Expression();
    }

    /**
     * @brief Funció per imitar l'avaluació d'una expressió
     * @details Implementem un mètode per poder fer tests que vulguin avaluar un contingut a partir d'una expressió booleana
     * @param content amb el contingut que es voldria avaluar
     * @param caseSensitive indicant si caldria fer una avaluació case sensitive o no
     * @return Retorna cert o fals en funció de si el contingut té una llargada major o menor/igual a 5, respectivament.
     */
    public Boolean evaluate(String content, Boolean caseSensitive) {
        return content.length() > 5;
    }
}
