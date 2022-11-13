package test.domain.expressions;

import main.domain.expressions.Or;
import test.domain.expressions.Expression;
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
        Expression e1 = Expression.create("true");
        Expression e2 = Expression.create("false");
        Or o1 = new Or(e1, e1);
        Or o2 = new Or(e1, e2);
        Or o3 = new Or(e2, e1);
        Or o4 = new Or(e2, e2);

        // es prova tota la taula de veritat de la connectiva
        assertTrue(o1.evaluate("content", false));
        assertTrue(o2.evaluate("content", false));
        assertTrue(o3.evaluate("content", false));
        assertFalse(o4.evaluate("content", false));
    }
}
