package main.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

/**
 * @class Expression
 * @brief Classe que representa i evalua expressions booleanes
 * @author marc.valls.camps i pau.duran.manzano
 */
public abstract class Expression {
    public static Expression create(String str) throws ExceptionInvalidExpression {
        System.out.println("Iniciando analasi de " + str);
        str = keys_to_ands(str);
        return initialize(str);
    }

    private static Expression initialize(String str) throws ExceptionInvalidExpression {
        if (str.isEmpty()) throw new ExceptionInvalidExpression(str);
        while (str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') str = str.substring(1, str.length()-1);
        while (!str.isEmpty() && str.charAt(0) == ' ') str = str.substring(1, str.length());
        while (!str.isEmpty() && str.charAt(str.length()-1) == ' ') str = str.substring(0, str.length()-1);
        if (str.isEmpty()) throw new ExceptionInvalidExpression(str);
        if (is_dual_operator(str.charAt(0))) throw new ExceptionInvalidExpression(str);
        if (is_dual_operator(str.charAt(str.length()-1))) throw new ExceptionInvalidExpression(str);
        if (!checkComillas(str)) throw new ExceptionInvalidExpression(str);
        if (!checkParentesis(str)) throw new ExceptionInvalidExpression(str);
        System.out.println("Comillas, parenesis, operadors i espais... checked");

        for(int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '"') while (str.charAt(++i) != '"');
            if (str.charAt(i) == '(') {
                int diff = 1;
                while (diff != 0) {
                    if (str.charAt(++i) == '(') diff++;
                    if (str.charAt(i) == ')') diff--;
                }
            }
            if (str.charAt(i) == '|') {
                System.out.println("Encontramos una | en la posicion " + i);
                String strleft = str.substring(0, i);
                String strright = str.substring(i+1, str.length());
                return new Or(create(strleft), create(strright));
            }
        }
        for(int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '"') while (str.charAt(++i) != '"');
            if (str.charAt(i) == '(') {
                int diff = 1;
                while (diff != 0) {
                    if (str.charAt(++i) == '(') diff++;
                    if (str.charAt(i) == ')') diff--;
                }
            }
            if (str.charAt(i) == '&') {
                System.out.println("Encontramos una & en la posicion " + i);
                String strleft = str.substring(0, i);
                String strright = str.substring(i+1, str.length());
                return new And(create(strleft), create(strright));
            }
        }
        if (str.charAt(0) == '!') {
            System.out.println("Encontramos una not !");
            String strNot = str.substring(1);
            return new Not(create(strNot));
        } else {
            if (str.charAt(0) == '"') {
                if (str.charAt(str.length()-1) == '"') {
                    System.out.println(" Literal con comitas");
                    String strLiteral = str.substring(1, str.length()-1);
                    return new Literal(strLiteral);
                } else throw new ExceptionInvalidExpression(str);
            }
            else {
                System.out.println("Literal");
                return new Literal(str);
            }
        }
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
                    while (j < i && exp.charAt(j) == ' ') ++j;                      // elimina espais al principi del Literal
                    if (j < i) {
                        int jj = j;
                        if (exp.charAt(j) == '"') {
                            ++j;
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
            }
            else if (exp.charAt(i) == '}') throw new ExceptionInvalidExpression(exp); // claus no tanquen
            else result += String.valueOf(exp.charAt(i));
        }
        return result;
    }

    private static boolean is_dual_operator(Character a) {
        return (a == '&' || a == '|');
    } 

    private static boolean checkParentesis(String str) {
        boolean correct = true;
        boolean first = true;
        int i = 0;
        int open = 0;
        int close = 0;
        while (correct && i < str.length()) {
            if (str.charAt(i) == '"') while (str.charAt(++i) != '"');
            if (str.charAt(i) == '(') {
                open++;
                first = false;
            }
            if (str.charAt(i) == ')') {
                if (first) return false;
                close++;
            }
            if (open < close) return false;
            i++;
        }
        return correct && open == close;
    }

    private static boolean checkComillas(String str) {
        int total = 0;
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '"') total++;
            i++;
        }
        return (total % 2 == 0);
    }

    public abstract boolean evaluate(String content, boolean caseSensitive);
}