package test.domain.expressions;

import main.domain.expressions.Expression;

import main.excepcions.ExceptionInvalidExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @class TestExpression
 * @brief Classe per provar de forma unitària la classe Expression
 * @author marc.valls.camps
 */

public class TestExpression {

    // CREATION TESTS
    @Test
    public void rareCharacters() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" -*//$$% & %%12");
    }

    @Test
    public void simpleNot() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" ! abcd ");
    }

    @Test
    public void simpleAnd() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" abcd & abcd ");
    }

    @Test
    public void simpleOr() throws ExceptionInvalidExpression {
        Expression a = Expression.create(" abcd | abcd ");
    }

    @Test
    public void noSpaces() throws ExceptionInvalidExpression {
        Expression a = Expression.create("a&b&c|(d|!e)");
    }

    @Test
    public void manySpaces() throws ExceptionInvalidExpression {
        Expression a = Expression.create("     ( abcd)     & (abcd|  abcd   )  ");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void quotes1() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\" abcd \" \" efgh \"");
    }

    @Test
    public void quotes2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("abcd | \"abcd abcd\" & abcd ");
    }

    @Test
    public void quotes3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("abcd & \"( abcd\"");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void quotes4() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\"abcd | \"abcd\"");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void quotes5() throws ExceptionInvalidExpression {
        Expression a = Expression.create("abcd\"");
    }

    @Test
    public void quotes6() throws ExceptionInvalidExpression {
        Expression a = Expression.create("\"abcd | abcd\"");
    }

    @Test
    public void keys1() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{abcd \"abcd abcd\"} & abcd & {abcd \"abcd abcd\"}");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void keys2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ { abcd }");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void keys3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ { abcd } }");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void keys4() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ abcd } {");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void keys5() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ abcd");
    }

    @Test
    public void parentheses1() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(a & (b | c))");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void parentheses2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(a (b | c))");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void parentheses3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(a & (b | c)");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void parentheses4() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(a & (b | c)))");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void parentheses5() throws ExceptionInvalidExpression {
        Expression a = Expression.create(")a & (b | c)");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void operandsAreNotWords1() throws ExceptionInvalidExpression {
        Expression a = Expression.create("hola !");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void operandsAreNotWords2() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{ | abcd}");
    }

    @Test(expected = ExceptionInvalidExpression.class)
    public void operandsAreNotWords3() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(abcd & (hola | &))");
    }

    // COMPLEX EVALUATE TESTS
    @Test
    public void exampleInStatement() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{p1 p2 p3} & (\"hola adéu\" | pep) & !joan");

        assertTrue(a.evaluate("p3 pep p1 abcd p2", false));
        assertFalse(a.evaluate("p3 pep p1 joan abcd p2", false));
        assertTrue(a.evaluate("p3 pep p1 joanet abcd p2", false));
        assertTrue(a.evaluate("p3 p1 hola adéu p2", false));
        assertTrue(a.evaluate("p3 (p1) hola adéu, p2!", false));
        assertFalse(a.evaluate("p3 p1 hola abcd adéu p2", false));
    }

    @Test
    public void prefixesInfixesSuffixesNotConsidered() throws ExceptionInvalidExpression {
        Expression a = Expression.create("(a | !b)");

        assertTrue(a.evaluate("barca", false));
        assertFalse(a.evaluate("b", false));
        assertTrue(a.evaluate("b a", false));
        assertFalse(a.evaluate("b hola", false));
        assertFalse(a.evaluate("b sac", false));
        assertFalse(a.evaluate("b aa", false));
    }

    @Test
    public void caseSensitive() throws ExceptionInvalidExpression {
        Expression a = Expression.create("{P1 p2 p3} & (\"hola adéu\" | pep) & !Joan");

        assertFalse(a.evaluate("p3 pep p1 abcd p2", true));
        assertTrue(a.evaluate("p3 pep P1 joan abcd p2", true));
        assertFalse(a.evaluate("p3 Pep P1 joan abcd p2", true));
        assertTrue(a.evaluate("p3 Pep P1 joan abcd p2 hola adéu", true));
        assertFalse(a.evaluate("p3 Pep P1 joan abcd p2 hola aDéu", true));
    }

    @Test
    public void naturalOrderOfOperandsRespected() throws ExceptionInvalidExpression {
        Expression a = Expression.create("a & b | c & d");

        assertTrue(a.evaluate("a b", false));
        assertTrue(a.evaluate("c d", false));
        assertFalse(a.evaluate("b c", false));
    }

    @Test
    public void parenthesesNotFollowingNaturalOrder() throws ExceptionInvalidExpression {
        Expression a = Expression.create("a & (b | c) & d");

        assertFalse(a.evaluate("a b", false));
        assertFalse(a.evaluate("c d", false));
        assertFalse(a.evaluate("b c", false));
    }
}
