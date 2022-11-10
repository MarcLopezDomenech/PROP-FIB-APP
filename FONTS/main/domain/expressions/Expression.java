package main.domain.expressions;

import main.excepcions.ExceptionInvalidExpression;

/**
 * @class Expression
 * @brief Classe que representa i evalua expressions booleanes
 * @author marc.valls.camps
 */
public abstract class Expression {
    
    private String value; 
    private Expression left = null;
    private Expression right = null;

    /*public static void main(String[] args) {
        try {
            Expression ex1 = new Expression(" ");
            System.out.println("EXPRESSIO VALIDA");
        } catch (ExceptionInvalidExpression e) {
            System.out.println("EXPRESSIO INVALIDA");
        }
    }*/

    public Expression(String str) throws ExceptionInvalidExpression {
        System.out.println("Iniciando analasi de " + str);
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

        boolean or = false; 
        boolean and = false; 
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
                value = "|";
                left = new Expression(strleft);
                right = new Expression(strright);
                or = true;
                break;
            }
        }
        if (!or) {
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
                    value = "&";
                    left = new Expression(strleft);
                    right = new Expression(strright);
                    and = true;
                    break;
                }
            }
        }
        if (!or && !and) {
            if (str.charAt(0) == '!') {
                System.out.println("Encontramos una not !");
                value = "!";
                left = new Expression(str.substring(1, str.length()));
            } else {
                if (str.charAt(0) == '"') {
                    if (str.charAt(str.length()-1) == '"') {
                        System.out.println(" Literal con comitas");
                        value = str.substring(1, str.length()-1);
                    } else {
                        throw new ExceptionInvalidExpression(str);
                    }
                }
                else {
                    System.out.println("Literal");
                    value = str;
                }
            }
        }
    }

    public boolean is_operator(Character a) {
        return (a=='!' || a == '&' || a == '|');
    } 

    public boolean is_dual_operator(Character a) {
        return (a == '&' || a == '|');
    } 

    public boolean checkParentesis(String str) {
        boolean correct = true;
        boolean first = true;
        int i = 0;
        int o = 0;
        int c = 0;
        while (correct && i < str.length()) {
            if (str.charAt(i) == '"') while (str.charAt(++i) != '"');
            if (str.charAt(i) == '(') {
                o++;
                first = false;
            }
            if (str.charAt(i) == ')') {
                if (first) return false;
                c++;
            }
            if (o < c) return false;
            i++;
        }
        return correct && o == c;
    }

    public boolean checkComillas(String str) {
        int total = 0;
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '"') total++;
            i++;
        }
        return (total % 2 == 0);
    }
}