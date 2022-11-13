package main.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

/**
 * @class Expression
 * @brief Classe que representa i avalua expressions booleanes
 * @author marc.valls.camps, ariadna.cortes.danes i pau.duran.manzano
 */
public abstract class Expression {
    /**
     * @brief Comprova si tanquen les cometes ""
     * @details Donat que el caràcter d'obrir i tancar cometes és el mateix, evidentment les cometes no són multinivell
     * @param expression String a analitzar
     * @return Cert ssi el nombre de cometes en el paràmetre expression és parell
     */
    private static boolean checkQuotes(String expression) {
        boolean quotes = true;
        for (int i = 0; i < expression.length(); ++i)
            if (expression.charAt(i) == '"') quotes = !quotes;
        return quotes;
    }

    /**
     * @brief Comprova si tanquen els parèntesis ()
     * @details Els parèntesis són multinivell, i es comprova que estiguin ben enniuats
     * @pre En el paràmetre expression les cometes "" tanquen
     * @param expression String a analitzar
     * @return Cert ssi els parèntesis tanquen, és a dir hi ha la mateixa quantitat de ( que de ),
     * i a més a més, estan ben enniuats
     */
    private static boolean checkParentheses(String expression) {
        int count = 0;
        for (int i = 0; i < expression.length(); ++i) {
            if (expression.charAt(i) == '"') while (expression.charAt(++i) != '"');
            else if (expression.charAt(i) == '(') ++count;
            else if (expression.charAt(i) == ')') --count;

            if (count < 0) return false;
        }
        return count == 0;
    }

    /**
     * @brief Comprova si tanquen les claus {}, i tradueix cadascun d'aquests operadors a la fórmula lògica equivalent amb &
     * @details Les claus no són multinivell, i es comprova que això es respecti
     * @pre En el paràmetre expression les cometes "" tanquen
     * @param expression String a analitzar
     * @return Una String que representa la mateixa expressió, havent fet la traducció de claus {} a un seguit de &
     * @throws ExceptionInvalidExpression Les claus {} no tanquen, o bé s'han intentat enniuar i per tant expression
     * no representa una expressió booleana vàlida
     */
    private static String keysToAnds(String expression) throws ExceptionInvalidExpression {
        int n = expression.length();
        String result = "";

        for (int i = 0; i < n; ++i) {
            if (expression.charAt(i) == '{'){
                int j = i + 1;
                while (i < n && expression.charAt(i) != '}') {
                    if (expression.charAt(i) == '"') while (expression.charAt(++i) != '"');
                    ++i;
                }
                if (i == n) throw new ExceptionInvalidExpression(expression);                           // claus no tanquen

                boolean operands = false;
                while (j < i) {
                    while (j < i && expression.charAt(j) == ' ') ++j;
                    if (j < i) {
                        int jj = j;
                        if (expression.charAt(j) == '"') {
                            while(j < i && expression.charAt(++j) != '"');
                            ++j;
                        }
                        else while(j < i && expression.charAt(j) != ' ') {
                            if (expression.charAt(j) == '{') throw new ExceptionInvalidExpression(expression);  // claus enniuades
                            ++j;
                        }
                        result += expression.substring(jj, j);
                        result += " & ";
                        operands = true;
                    }
                }
                if (operands) result = result.substring(0,result.length()-3);
            }
            else if (expression.charAt(i) == '}') throw new ExceptionInvalidExpression(expression);     // claus no tanquen
            else {
                if (expression.charAt(i) == '"') {
                    do { result += String.valueOf(expression.charAt(i)); ++i; } while (i < n && expression.charAt(i) != '"');
                    result += "\"";
                }
                else result += String.valueOf(expression.charAt(i));
            }
        }
        return result;
    }

    /**
     * @brief Desempaqueta l'expressió, eliminant espais i parèntesis
     * @pre En el paràmetre expression les cometes "" tanquen, i a més no conté claus {}
     * @param expression String a desempaquetar
     * @return L'expressió desempaquetada, és a dir, una String que representa una fórmula equivalent sense espai al principi o al final,
     * ni amb parèntesis envoltant tota l'expressió
     * @throws ExceptionInvalidExpression El paràmetre expression representa una fórmula lògica equivalent a la fórmula buida,
     * que no és una fórmula vàlida
     */
    private static String unpack(String expression) throws ExceptionInvalidExpression{
        if (expression.isEmpty()) throw new ExceptionInvalidExpression(expression);

        boolean changes = true;
        while (changes) {
            changes = false;
            if (!expression.isEmpty() && expression.charAt(0) == '(' && expression.charAt(expression.length()-1) == ')') {
                if (checkParentheses(expression.substring(1, expression.length()-1))) {
                    expression = expression.substring(1, expression.length()-1); changes = true;
                }
            }
            if (!expression.isEmpty() && expression.charAt(0) == ' ') {
                expression = expression.substring(1); changes = true;
            }
            if (!expression.isEmpty() && expression.charAt(expression.length()-1) == ' ') {
                expression = expression.substring(0, expression.length()-1); changes = true;
            }
        }

        if (expression.isEmpty()) throw new ExceptionInvalidExpression(expression);
        return expression;
    }

    /**
     * @brief Busca un operand en el nivell més extern de parèntesis
     * @pre En el paràmetre expression les cometes "" i els parèntesis () tanquen, i a més no conté claus {}
     * @param expression String on es busca l'operand
     * @param operand Char que representa l'operand que s'ha de buscar a expression
     * @return Retorna la posició del primer caràcter igual a operand dins de expression que no està afectat per cap parèntesis,
     * o bé -1 si no n'existeix cap
     */
    private static int findTopLevelOperand(String expression, char operand) {
        for(int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '"') while (expression.charAt(++i) != '"');
            if (expression.charAt(i) == '(') {
                int count = 1;
                while (count > 0) {
                    if (expression.charAt(++i) == '(') ++count;
                    if (expression.charAt(i) == ')') --count;
                }
            }
            if (expression.charAt(i) == operand) return i;
        }
        return -1;
    }

    /**
     * @brief Tradueix expression a una instància de Expression que representa la fórmula lògica del paràmetre, sempre que sigui vàlida
     * @pre En el paràmetre expression les cometes "" i els parèntesis () tanquen, i a més no conté claus {}
     * @param expression String a processar
     * @return Una instància d'Expressió que representa a una fórmula booleana equivalent a la d'expression
     * @throws ExceptionInvalidExpression El paràmetre expression no representa una expressió booleana vàlida
     */
    private static Expression recursiveDeconstruction(String expression) throws ExceptionInvalidExpression {
        expression = unpack(expression);
/*
        int i = findTopLevelOperand(expression, '|');
        if (i != -1) {
            String left = expression.substring(0, i);
            String right = expression.substring(i+1);
            return new Or(recursiveDeconstruction(left), recursiveDeconstruction(right));
        }

        i = findTopLevelOperand(expression, '&');
        if (i != -1) {
            String left = expression.substring(0, i);
            String right = expression.substring(i+1);
            return new And(recursiveDeconstruction(left), recursiveDeconstruction(right));
        }

        if (expression.charAt(0) == '!') {
            String inner = expression.substring(1);
            return new Not(recursiveDeconstruction(inner));
        }

        for (int k = 1; k < expression.length() - 1; ++k)
            if (expression.charAt(k) == '"') throw new ExceptionInvalidExpression(expression);

        if (expression.charAt(0) == '"') {
            if (expression.charAt(expression.length()-1) != '"') throw new ExceptionInvalidExpression(expression);
            String value = expression.substring(1, expression.length()-1);
            return new Literal(value);
        }

        for (int k = 1; k < expression.length() - 1; ++k)
         if (expression.charAt(k) == '!' || expression.charAt(k) == '|' || expression.charAt(k) == '&' || expression.charAt(k) == ' ')
             throw new ExceptionInvalidExpression(expression);

        return new Literal(expression);*/
        return null;
    }

    /**
     * @brief Tradueix expression a una instància de Expression que representa la fórmula lògica del paràmetre, sempre que sigui vàlida
     * @param expression String a processar
     * @return Una instància d'Expressió que representa a una fórmula booleana equivalent a la d'expression
     * @throws ExceptionInvalidExpression El paràmetre expression no representa una expressió booleana vàlida
     */
    public static Expression create(String expression) throws ExceptionInvalidExpression {
        // quotes close test
        if (!checkQuotes(expression)) throw new ExceptionInvalidExpression(expression);
        // parentheses close test
        if (!checkParentheses(expression)) throw new ExceptionInvalidExpression(expression);
        // keys close test and translation
        expression = keysToAnds(expression);

        return recursiveDeconstruction(expression);
    }

    /**
     * @brief Avaluació lògica de l'expressió
     * @param content Contingut on buscar i sobre el que s'evalua l'expressió
     * @param caseSensitive Indica si és necessari diferenciar majúscules de minúscules
     * @return Cert ssi l'Expression implícita avalua a cert per al contingut content, no es consideren prefixos, infixos ni
     * sufixos, i el paràmetre caseSensitive indica si s'ha fet diferència entre majúscules i minúscules
     */
    public abstract boolean evaluate(String content, boolean caseSensitive);
}