package test.domain.expressions;

import test.domain.expressions.Expression;

/**
 * @class False (stub)
 * @brief Classe que usem per provar expressions, sabent que sempre ens retornarà una avaluació falsa
 * @author pau.duran.manzano
 */
public class False extends Expression {
    /**
     * @brief Constructora per defecte
     * @return Instància de True
     */
    public False() {
    }

    /**
     * @brief Funció que sobreescriu el evaluate de Expression (stub)
     * @return false, sigui quins siguin els paràmetres
     */
    public Boolean evaluate(String content, Boolean caseSensitive) {
        return false;
    }
}
