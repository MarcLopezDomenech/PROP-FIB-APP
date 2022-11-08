package main.domain.expressions;

/**
 * @class And
 * @brief Classe que representa la conjunció de dues expressions booleanes
 * @author marc.valls.camps
 */
public class And extends Expression{
    private Expression op1;
    private Expression op2;

    And(Expression op1, Expression op2){
        this.op1 = op1;
        this.op2 = op2;
    }

    public And() {

    }

    public boolean evaluate(String content, boolean caseSensitive){
        return op1.evaluate(content, caseSensitive) && op2.evaluate(content, caseSensitive);
    }
}
