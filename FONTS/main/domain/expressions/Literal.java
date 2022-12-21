package main.domain.expressions;

/**
 * @class Literal
 * @brief Classe que representa a aquelles expressions booleanes formades solament per una paraula o frase, sense cap connectiva lògica
 * @author marc.valls.camps
 */
public class Literal extends Expression {
    /** \brief String que guarda la paraula o frase representada pel Literal */
    private String value;

    /**
     * @brief Constructora per valor
     * @param value String que representa el Literal
     * @return Una instància de Literal que representa l'expressió formada únicament per la String paràmetre value
     */
    public Literal(String value){
        this.value = value;
        this.height = 0;
    }

    /**
     * @brief Funció booleana que retorna si el paràmetre c és un caràcter especial de text
     * @details Un caràcter de text és un caràcter que és natural trobar en un escrit. Els que es consideraran especials
     * són ' ', '.', ',', ';', '!', '?', '(', ')', ':' i '\n'
     * @param c Caràcter a analitzar
     * @return Cert ssi el paràmetre c és un caràcter especial de text
     */
    private static boolean is_special_text_char(char c) {
        return c == ' ' || c == '.' || c == ',' || c == ';' || c == '!' || c == '?' || c == '(' || c == ')' || c == ':' || c == '\n';
    }

    /**
     * @brief Avaluació del Literal
     * @param content Contingut on buscar
     * @param caseSensitive Indica si és necessari diferenciar majúscules de minúscules
     * @return Cert ssi la String que representa el Literal implícit és a dins del paràmetre content, no es consideren
     * prefixos, infixos ni sufixos, i el paràmetre caseSensitive indica si s'ha fet diferència entre majúscules i minúscules
     */
    public boolean evaluate(String content, boolean caseSensitive){
        String content_to_evaluate = " " + content  + " ";
        String value_to_find = value;

        if (!caseSensitive) {
            content_to_evaluate = content_to_evaluate.toLowerCase();
            value_to_find = value_to_find.toLowerCase();
        }

        int l = value_to_find.length();
        int n = content_to_evaluate.length();
        for (int i = 0; i <= n - (l + 2); ++i)
            if (is_special_text_char(content_to_evaluate.charAt(i)) &&
                value_to_find.equals(content_to_evaluate.substring(i+1, i+1+l)) &&
                is_special_text_char(content_to_evaluate.charAt(i+1+l))) return true;

        return false;
    }
}
