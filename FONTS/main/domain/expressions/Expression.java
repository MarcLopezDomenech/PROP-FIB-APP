package main.domain.expressions;

/**
 * @class Expression
 * @brief Classe que representa i evalua expressions booleanes
 * @author marc.valls.camps
 */
public abstract class Expression {
    public static boolean verify(String exp) {
        // format test
        // hi ha dhaver caracters que no acceptem?

        // close test
        int pars = 0;
        int keys = 0;
        //int
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == '(') ++pars;
            else if (exp.charAt(i) == ')') --pars;
            else if (exp.charAt(i) == '{') ++keys;
            else if (exp.charAt(i) == '}') --keys;

            if ((exp.charAt(i) == '(' || exp.charAt(i) == ')') && keys == 1) return false;
            if (pars < 0 || keys < 0 || keys > 1) return false;
        }
        if (pars != 0 || keys != 0) return false;

        // key substitution
        for (int i = exp.length() - 1; i >= 0; --i) {
            if (exp.charAt(i) == '}') {
                int j = i;
                while (exp.charAt(i) != '{') --i;

            }
        }

        // space elimination - CAN'T BE DONE THIS WAY (phrases)
        exp.replaceAll("\\s+","");

        // consecutive operators test - relies on space elimination
        while (exp.contains("!!")) exp.replaceAll("!!", "");
        if (exp.contains("&&") || exp.contains("||") || exp.contains("&|") ||
                exp.contains("|&") || exp.contains("!|") || exp.contains("!&")) return false;

        // consecutive words test?


        return true;
    }

    public static Expression create(String exp) {
        return null;
    }

    public abstract boolean evaluate(String content, boolean caseSensitive);



    /**
     * @class Not
     * @brief Classe que representa la negació d'una expressió booleana
     * @author marc.valls.camps
     */
    private class Not extends Expression {
        private Expression op1;

        Not(Expression op1){
            this.op1 = op1;
        }

        public boolean evaluate(String content, boolean caseSensitive){
            return !op1.evaluate(content, caseSensitive);
        }
    }

    /**
     * @class And
     * @brief Classe que representa la conjunció de dues expressions booleanes
     * @author marc.valls.camps
     */
    private class And extends Expression{
        private Expression op1;
        private Expression op2;

        And(Expression op1, Expression op2){
            this.op1 = op1;
            this.op2 = op2;
        }

        public boolean evaluate(String content, boolean caseSensitive){
            return op1.evaluate(content, caseSensitive) && op2.evaluate(content, caseSensitive);
        }
    }

    /**
     * @class Or
     * @brief Classe que representa la disjunció de dues expressions booleanes
     * @author marc.valls.camps
     */
    private class Or extends Expression {
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

    /**
     * @class Literal
     * @brief Classe que representa a les paraules o frases que composen una expressió booleana
     * @author marc.valls.camps
     */
    private class Literal extends Expression {
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
}