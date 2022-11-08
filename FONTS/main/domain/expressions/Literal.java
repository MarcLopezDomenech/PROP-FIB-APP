package main.domain.expressions;

/**
 * @class Literal
 * @brief Classe que representa a les paraules o frases que composen una expressió booleana
 * @author marc.valls.camps
 */
public class Literal extends Expression {
    String valor;

    Literal(String valor){
        this.valor = valor;
    }

    /**
     * @return Evalua
     * @param
     */
    public boolean evaluate(String content, boolean caseSensitive){
        // no es correcte, cal corregir
        // falta caseSensitive
        return content.contains(" " + valor + " ") || content.contains(" " + valor + ".") ||
                content.contains(" " + valor + ",") || content.contains(" " + valor + ";") ||
                content.contains(" " + valor + "!") || content.contains(" " + valor + "?");
    }
}
