package main.domain.expressions;

import main.domain.expressions.Expression;

/**
 * @class Or
 * @brief Classe que representa a aquelles expressions booleanes que consisteixen en la disjunció de dues altres expressions lògiques
 * @author marc.valls.camps
 */
public class Or extends Expression {
    /** \brief Atribut que representa la primera subexpressió de la disjunció */
    private Expression left;
    /** \brief Atribut que representa la segona subexpressió de la disjunció */
    private Expression right;

    /**
     * @brief Constructora per valor
     * @param left Expressió que representa la primera subexpressió de la disjunció
     * @param right Expressió que representa la segona subexpressió de la disjunció
     * @return Una instància de Or que representa la disjunció lògica de les expressions left i right
     */
    public Or(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    /**
     * @brief Avaluació de la Or
     * @param content Contingut on buscar
     * @param caseSensitive Indica si és necessari diferenciar majúscules de minúscules
     * @return Cert ssi la Or implícita avalua a cert per al contingut content, no es consideren prefixos, infixos ni
     * sufixos, i el paràmetre caseSensitive indica si s'ha fet diferència entre majúscules i minúscules
     */
    public boolean evaluate(String content, boolean caseSensitive){
        return left.evaluate(content, caseSensitive) || right.evaluate(content, caseSensitive);
    }
}
