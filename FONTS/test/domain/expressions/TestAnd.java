package test.domain.expressions;

import main.domain.expressions.And;
import main.excepcions.ExceptionInvalidExpression;
import main.domain.expressions.Expression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestAnd
 * @brief Classe per provar de forma unitària la classe And
 * @author marc.valls.camps i pau.duran.manzano
 */

public class TestAnd {
    @Test
    public void basic_constructor() throws ExceptionInvalidExpression {
        Expression evalTrue = Expression.create("true");
        Expression evalFalse = Expression.create("false");
        And tt = new And(evalTrue, evalTrue);
        And tf = new And(evalTrue, evalFalse);
        And ft = new And(evalFalse, evalTrue);
        And ff = new And(evalFalse, evalFalse);

        // es prova tota la taula de veritat de la connectiva
        assertTrue(tt.evaluate("content", false));
        assertFalse(tf.evaluate("content", false));
        assertFalse(ft.evaluate("content", false));
        assertFalse(ff.evaluate("content", false));
    }
}
