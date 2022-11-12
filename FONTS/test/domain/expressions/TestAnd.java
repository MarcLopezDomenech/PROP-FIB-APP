package test.domain.expressions;

import main.domain.expressions.Literal;
import main.domain.expressions.And;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestAnd
 * @brief Classe per provar de forma unitària la classe And
 * @author marc.valls.camps
 */

public class TestAnd {
    @Test
    public void basic_constructor() {
        Literal l1 = new Literal("abcd");
        Literal l2 = new Literal("efgh");
        And a = new And(l1, l2);

        // es prova tota la taula de veritat de la connectiva
        assertFalse(a.evaluate("nothing", false));
        assertFalse(a.evaluate("abcd other", false));
        assertFalse(a.evaluate("efgh other", false));
        assertTrue(a.evaluate("abcd efgh", false));
    }
}
