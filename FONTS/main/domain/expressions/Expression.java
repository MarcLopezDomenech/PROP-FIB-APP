package main.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Expression
 * @brief Classe que representa i evalua expressions booleanes
 * @author marc.valls.camps i pau.duran.manzano
 */
public abstract class Expression {
    /*public static boolean verify(String exp) {
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
    }*/

    public static void main(String[] args) throws ExceptionInvalidExpression {
        Expression e = create("hola & !(adeu) & pa&&tata");
    }

    public static Expression create(String exp) throws ExceptionInvalidExpression {
        int n = exp.length();
        String result = new String();
        for (int i = 0; i < n; ++i) {
            if (exp.charAt(i) == '{') {
                result += exp.substring(0, i);
                ++i;
                int ini = i;
                Boolean found = false;
                while (i < n && exp.charAt(i) != '}') {
                    if (exp.charAt(i) == ' ') {
                        found = true;
                        result += exp.substring(ini, i);
                        result += " & ";
                        ini = i + 1;
                    }
                    ++i;
                }
                if (i == n) throw new ExceptionInvalidExpression(exp);
                if (found) result = result.substring(0, result.length() - 3);
                ++i;
            }
        }
        if (result.length() == 0) result = exp;
        return create1(result);
    }

    public static Expression create1(String exp) throws ExceptionInvalidExpression {
        int parentesis = 0;
        Boolean cometes = false;
        Boolean tocaOp = false;
        int firstOr = -1;
        int firstAnd = -1;

        List<String> list = new ArrayList<>();

        int n = exp.length();
        int ini = 0;
        for (int i = 0; i < n; ++i) {
            char c = exp.charAt(i);
            if (tocaOp && c != ' ') {
                if (c == '&') {
                    if (firstAnd == -1) firstAnd = list.size();
                }
                else if (c == '|') {
                    if (firstOr == -1) firstOr = list.size();
                }
                else throw new ExceptionInvalidExpression(exp);
            }
            if (c == '(') {
                if (!cometes) ++parentesis;
            }
            else if (c == ')') {
                if (!cometes) {
                    --parentesis;
                    if (parentesis < 0) ; // ToDo: throw exception
                }
            }
            else if (c == '"') cometes = !cometes;
            else if ((c == ' ' || i == (n - 1)) && parentesis == 0 && !cometes) {
                if (i == n - 1) ++i;
                list.add(exp.substring(ini, i));
                ini = i + 1;
                tocaOp = !tocaOp;
            }
        }
        if (!tocaOp || parentesis != 0 || cometes); // ToDo: throw exception

        if (firstOr != -1) {
            Expression left = create2(list.subList(0, firstOr));
            Expression right = create2(list.subList(firstOr + 1, list.size()));
            return new Or(left, right);
        }
        else if (firstAnd != -1) {
            Expression left = create2(list.subList(0, firstAnd));
            Expression right = create2(list.subList(firstAnd + 1, list.size()));
            return new And(left, right);
        }
        else {
            return create3(list.get(0));
        }
    }

    public static Expression create2(List<String> list) throws ExceptionInvalidExpression {
        if (list.size() == 1) return create3(list.get(0));
        int firstAnd = -1;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            if ("|".equals(list.get(i))) {
                Expression left = create2(list.subList(0, i));
                Expression right = create2(list.subList(i+1, n));
                return new Or(left, right);
            }
            else if ("&".equals(list.get(i)) && firstAnd == -1) firstAnd = i;
        }
        if (firstAnd == -1) throw new ExceptionInvalidExpression("NOOOOOOO");
        else {
            Expression left = create2(list.subList(0, firstAnd));
            Expression right = create2(list.subList(firstAnd + 1, list.size()));
            return new And(left, right);
        }
    }

    public static Expression create3(String exp) throws ExceptionInvalidExpression {
        switch (exp.charAt(0)) {
            case '!':
                Expression expr = create3(exp.substring(1));
                return new Not(expr);
            case '(':
                if (exp.charAt(exp.length()-1) != ')') throw new ExceptionInvalidExpression(exp);
                return create3(exp.substring(1, exp.length()-1));
            case '"':
                if (exp.charAt(exp.length()-1) != '"') throw new ExceptionInvalidExpression(exp);
                return create3(exp.substring(1, exp.length()-1));
            default:
                return new Literal(exp);
        }
    }

    public abstract boolean evaluate(String content, boolean caseSensitive);

}