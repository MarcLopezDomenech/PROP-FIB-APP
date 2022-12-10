package main.domain.expressions;

import main.domain.expressions.Expression;

/**
 * @class Not
 * @brief Classe que representa a aquelles expressions booleanes que consisteixen en la negació d'una altra expressió lògica
 * @author marc.valls.camps
 */
public class Not extends Expression {
    /** \brief Atribut que representa la subexpressió negada */
    private Expression inner;

    /**
     * @brief Constructora per valor
     * @param inner Expressió que representa la subexpressió negada
     * @return Una instància de Not que representa la negació lògica de l'expressió inner
     */
    public Not(Expression inner) {
        this.inner = inner;
        this.height = inner.height + 1;
    }

    /**
     * @brief Avaluació de la Not
     * @param content Contingut on buscar
     * @param caseSensitive Indica si és necessari diferenciar majúscules de minúscules
     * @return Cert ssi la Not implícita avalua a cert per al contingut content, no es consideren prefixos, infixos ni
     * sufixos, i el paràmetre caseSensitive indica si s'ha fet diferència entre majúscules i minúscules
     */
    public boolean evaluate(String content, boolean caseSensitive) {
        return !inner.evaluate(content, caseSensitive);
    }
}
