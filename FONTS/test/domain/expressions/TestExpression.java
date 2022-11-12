package test.domain.expressions;

import main.domain.expressions.Expression;

import main.excepcions.ExceptionInvalidExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @class TestExpressionsSet
 * @brief Classe per provar de forma unitària la classe Expression
 * @author marc.valls.camps
 */

public class TestExpression {
    @Test
    public void caracters_estranys() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" -*//$$% & %%12");
    }

    @Test
    public void and_simple() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" abcd & abcd ");
    }

    @Test
    public void or_simple() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" abcd | abcd ");
    }

    @Test
    public void sense_espais() throws ExceptionInvalidExpression {
        Expression a = Expression.create("a&b&c|d|e");
    }

    @Test
    public void molts_espais() throws ExceptionInvalidExpression {
        Expression a = Expression.create("     abcd     & abcd|  abcd     ");
    }

    @Test
    public void cometes1() throws ExceptionInvalidExpression {
        Expression a = Expression.create("abcd | \"abcd abcd\" & abcd ");
    }

    @Test
    public void cometes2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("abcd & \"( abcd\"");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void cometes3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\"abcd | \"abcd\"");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void cometes4() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\"abcd");
    }

    @Test
    public void cometes5() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\"abcd | abcd\"");
    }

    @Test
    public void claus() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{abcd \"abcd abcd\"} & abcd & {abcd \"abcd abcd\"}");
    }

    @Test
    public void claus2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ { abcd }");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void claus3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ abcd } }");
    }

    @Test
    public void eval_claus2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ { abcd }");
        assertTrue(a.evaluate("efgh abcd {", false));
    }

    @Test
    public void eval_exemple() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{p1 p2 p3} & (\"hola adéu\" | pep) & !joan");
        assertTrue(a.evaluate("p3 pep p1 abcd p2", false));
        assertFalse(a.evaluate("p3 pep p1 joan abcd p2", false));
        assertTrue(a.evaluate("p3 pep p1 joanet abcd p2", false));
        assertTrue(a.evaluate("p3 p1 hola adéu p2", false));
        assertTrue(a.evaluate("p3 (p1) hola adéu, p2!", false));
        assertFalse(a.evaluate("p3 p1 hola abcd adéu p2", false));
    }

    // fer tests amb lowercase
    // fer tests amb prefixs, infixs, sufixs
    // fer tests que comprovin la jerarquia natural dels operadors
    // fer tests que comprovin que els parentesis poden alterar la jerarquia
}
