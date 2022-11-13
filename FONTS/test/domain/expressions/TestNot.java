package test.domain.expressions;

import main.domain.expressions.Not;
import main.excepcions.ExceptionInvalidExpression;
import test.domain.expressions.Expression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @class TestNot
 * @brief Classe per provar de forma unitària la classe Not
 * @author marc.valls.camps i pau.duran.manzano
 */

public class TestNot {
    @Test
    public void basic_constructor() throws ExceptionInvalidExpression {
        Expression evalTrue = Expression.create("true");
        Expression evalFalse = Expression.create("false");
        Not t = new Not(evalTrue);
        Not f = new Not(evalFalse);

        // es prova tota la taula de veritat de la connectiva
        assertFalse(t.evaluate("content", false));
        assertTrue(f.evaluate("content", true));
    }
}
