package test.domain.expressions;

/**
 * @class Expression (stub)
 * @brief Classe stub de Expression
 * @author pau.duran.manzano
 */
public class Expression {
    private String id;

    public Expression(String expression) {this.id = expression;}

    public String getId() {return id;}

    public Boolean evaluate(String content, Boolean caseSensitive) {return true;}

}
