package main.domain.expressions;

/**
 * @class Not
 * @brief Classe que representa la negació d'una expressió booleana
 * @author marc.valls.camps
 */
public class Not extends Expression {
    private Expression op1;

    Not(Expression op1){
        this.op1 = op1;
    }

    public boolean evaluate(String content, boolean caseSensitive){
        return !op1.evaluate(content, caseSensitive);
    }
}
