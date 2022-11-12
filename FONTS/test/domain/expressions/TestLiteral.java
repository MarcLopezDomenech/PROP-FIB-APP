package test.domain.expressions;

import main.domain.expressions.Literal;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestLiteral
 * @brief Classe per provar de forma unitària la classe Literal
 * @author marc.valls.camps
 */

public class TestLiteral {
    @Test
    public void basic_constructor() {
        Literal l = new Literal("abcd");

        assertTrue(l.evaluate("abcd", false));
        assertFalse(l.evaluate("efgh", false));
    }

    @Test
    public void eval_prefixes_infixes_suffixes() {
        Literal l = new Literal("bcd");

        assertTrue(l.evaluate("a bcd: e", false));
        assertFalse(l.evaluate("bcde", false));
        assertFalse(l.evaluate("abcd", false));
        assertFalse(l.evaluate("abcde", false));
    }

    @Test
    public void evaluate_special_chars() {
        Literal l = new Literal("abc");

        assertTrue(l.evaluate("abc", false));
        assertTrue(l.evaluate(" abc ", false));
        assertTrue(l.evaluate("(abc)", false));
        assertTrue(l.evaluate("abc: def", false));
        assertTrue(l.evaluate("abc.", false));
        assertTrue(l.evaluate("abc!", false));
        assertTrue(l.evaluate("abc?", false));
        assertTrue(l.evaluate("abc, def", false));
        assertTrue(l.evaluate("abc; def", false));
        assertFalse(l.evaluate("abcdef", false));
    }

    @Test
    public void evaluate_case_sensitive() {
        Literal l = new Literal("abc");
        Literal l2 = new Literal("ABC");
        Literal l3 = new Literal("aBc");

        assertTrue(l.evaluate("AbC", false));
        assertTrue(l2.evaluate("AbC", false));
        assertTrue(l3.evaluate("AbC", false));

        assertFalse(l.evaluate("AbC", true));
        assertFalse(l2.evaluate("AbC", true));
        assertFalse(l3.evaluate("AbC", true));

        Literal l4 = new Literal("AbC");
        assertTrue(l4.evaluate("AbC", true));
    }
}
