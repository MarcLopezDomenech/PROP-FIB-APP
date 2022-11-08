package main.domain.expressions;

/**
 * @class Or
 * @brief Classe que representa la disjunció de dues expressions booleanes
 * @author marc.valls.camps
 */
public class Or extends Expression {
    private Expression op1;
    private Expression op2;

    Or(Expression op1, Expression op2){
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * @return Evalua
     * @param
     */
    public boolean evaluate(String content, boolean caseSensitive){
        return op1.evaluate(content, caseSensitive) || op2.evaluate(content, caseSensitive);
    }
}
