package test.domain.expressions;

import main.domain.expressions.Literal;
import main.domain.expressions.Not;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestNot
 * @brief Classe per provar de forma unitària la classe Not
 * @author marc.valls.camps
 */

public class TestNot {
    @Test
    public void basic_constructor() {
        Literal l = new Literal("abcd");
        Not n = new Not(l);

        // es prova tota la taula de veritat de la connectiva
        assertFalse(n.evaluate("abcd", false));
        assertTrue(n.evaluate("efgh", false));
    }
}
