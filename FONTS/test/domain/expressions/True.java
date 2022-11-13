package test.domain.expressions;

import test.domain.expressions.Expression;

/**
 * @class True (stub)
 * @brief Classe que usem per provar expressions, sabent que sempre ens retornarà una avaluació certa
 * @author pau.duran.manzano
 */
public class True extends Expression {
    /**
     * @brief Constructora per defecte
     * @return Instància de True
     */
    public True() {
    }

    /**
     * @brief Funció que sobreescriu el evaluate de Expression (stub)
     * @return true, sigui quins siguin els paràmetres
     */
    public Boolean evaluate(String content, Boolean caseSensitive) {
        return true;
    }
}
