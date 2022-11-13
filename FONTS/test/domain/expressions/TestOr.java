package test.domain.expressions;

import main.domain.expressions.Or;
import main.excepcions.ExceptionInvalidExpression;
import main.domain.expressions.Expression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestOr
 * @brief Classe per provar de forma unitària la classe Or
 * @author marc.valls.camps i pau.duran.manzano
 */

public class TestOr {
    @Test
    public void basic_constructor() throws ExceptionInvalidExpression {
        Expression evalTrue = Expression.create("true");
        Expression evalFalse = Expression.create("false");
        Or tt = new Or(evalTrue, evalTrue);
        Or tf = new Or(evalTrue, evalFalse);
        Or ft = new Or(evalFalse, evalTrue);
        Or ff = new Or(evalFalse, evalFalse);

        // es prova tota la taula de veritat de la connectiva
        assertTrue(tt.evaluate("content", false));
        assertTrue(tf.evaluate("content", false));
        assertTrue(ft.evaluate("content", false));
        assertFalse(ff.evaluate("content", false));
    }
}
