package main.domain.expressions;

/**
 * @class And
 * @brief Classe que representa a aquelles expressions booleanes que consisteixen en la conjunció de dues altres expressions lògiques
 * @author marc.valls.camps
 */
public class And extends Expression{
    /** \brief Atribut que representa la primera subexpressió de la conjunció */
    private Expression left;
    /** \brief Atribut que representa la segona subexpressió de la conjunció */
    private Expression right;

    /**
     * @brief Constructora per valor
     * @param left Expressió que representa la primera subexpressió de la conjunció
     * @param right Expressió que representa la segona subexpressió de la conjunció
     * @return Una instància de And que representa la conjunció lògica de les expressions left i right
     */
    public And(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    /**
     * @brief Avaluació de la And
     * @param content Contingut on buscar
     * @param caseSensitive Indica si és necessari diferenciar majúscules de minúscules
     * @return Cert ssi la And implícita avalua a cert per al contingut content, no es consideren prefixos, infixos ni
     * sufixos, i el paràmetre caseSensitive indica si s'ha fet diferència entre majúscules i minúscules
     */
    public boolean evaluate(String content, boolean caseSensitive){
        return left.evaluate(content, caseSensitive) && right.evaluate(content, caseSensitive);
    }
}
