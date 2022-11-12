package test.domain.expressions;

import main.domain.expressions.Or;
import main.domain.expressions.Literal;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestOr
 * @brief Classe per provar de forma unitària la classe Or
 * @author marc.valls.camps
 */

public class TestOr {
    @Test
    public void basic_constructor() {
        Literal l1 = new Literal("abcd");
        Literal l2 = new Literal("efgh");
        Or o = new Or(l1, l2);

        // es prova tota la taula de veritat de la connectiva
        assertFalse(o.evaluate("nothing", false));
        assertTrue(o.evaluate("abcd other", false));
        assertTrue(o.evaluate("efgh other", false));
        assertTrue(o.evaluate("abcd efgh", false));
    }
}
