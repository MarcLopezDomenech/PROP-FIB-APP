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
    public static void main(String[] args) throws ExceptionInvalidExpression {
        Expression e = create("hola & !(adeu) & pa&&tata");
    }
    private static String keys_to_ands(String exp) throws ExceptionInvalidExpression {
        int n = exp.length();
        String result = "";
        for(int i = 0; i < n; ++i) {
            if (exp.charAt(i) == '{'){
                int j = i + 1;                                                      // apunta primer char despres de {
                while (i < n && exp.charAt(i) != '}') ++i;                          // desplaça i fins trobar }
                if (i == n) throw new ExceptionInvalidExpression(exp);              // claus no tanquen

                int count = 0;
                while (j < i) {
                    while (j < i && exp.charAt(i) == ' ') ++j;                      // elimina espais al principi del Literal
                    if (j < i) {
                        int jj = j;
                        if (exp.charAt(j) == '"') {
                            while(j < i && exp.charAt(j) != '"') ++j;
                            if (j == i) throw new ExceptionInvalidExpression(exp);  // cometes no tanquen
                            ++j;                                                    // per incloure les " que tanquen
                        }
                        // else if (exp.charAt(j) == '(') // es poden posar controls, de moment saccepta ( i ) com a paraula
                        else while(j < i && exp.charAt(j) != ' ') ++j;
                        result += exp.substring(jj, j);
                        result += " & ";
                        ++count;
                    }
                }
                if (count > 0) result = result.substring(0,result.length()-3);
                ++i;                                                                // perque estava apuntant a }
            }
            else if (exp.charAt(i) == '}') throw new ExceptionInvalidExpression(exp); // claus no tanquen
            else result += String.valueOf(exp.charAt(i));
        }
        return result;
    }

    public static Expression create(String exp) throws ExceptionInvalidExpression {
        String result = keys_to_ands(exp);
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