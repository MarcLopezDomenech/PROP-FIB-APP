package test.domain.expressions;

import java.util.HashMap;
import java.util.Map;

import main.excepcions.ExceptionExpressionExists;
import main.excepcions.ExceptionNoExpression;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import main.domain.expressions.ExpressionsSet;

public class TestExpressionsSet {
    private static Map<String, Expression> testValues;

    @Before
    public void ini() {
        testValues = new HashMap<>();
        testValues.put("Primera expressió", new Expression("Primera expressió"));
        testValues.put("Segona expressió", new Expression("Segona expressió"));
        testValues.put("Tercera i última expressió", new Expression("Tercera i última expressió"));
    }

    @Test
    public void testGetInstance() {
        ExpressionsSet e1 = ExpressionsSet.getInstance();
        ExpressionsSet e2 = ExpressionsSet.getInstance();
        assertSame(e1, e2);
    }

    @Test
    public void testGetISetExpressions() {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        Map<String, Expression> returnValue = es.getExpressions();
        assertSame(testValues, returnValue);
    }

    @Test
    public void testGetExpressionCorrect() throws ExceptionNoExpression {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        Expression returnValue = es.getExpression("Primera expressió");
        // Ens assegurem que el valor retornat sigui correcte i que les expression guardades no s'han modificat
        assertEquals(testValues.get("Primera expressió"), returnValue);
        assertEquals(testValues, es.getExpressions());
    }

    @Test (expected = ExceptionNoExpression.class)
    public void testGetExpressionException() throws ExceptionNoExpression {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.getExpression("Expressió inventada");
        System.out.println("Això no apareix per pantalla");
    }

    @Test
    public void testCreateExpressionCorrect() throws ExceptionExpressionExists {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.createExpression("Nova expressió");
        Map<String, Expression> returnValue = es.getExpressions();
        // Assegurem que s'ha creat l'entrada i que el valor associat és l'expressió correcta
        assertTrue(returnValue.containsKey("Nova expressió"));
        assertTrue(returnValue.get("Nova expressió").getId().equals("Nova expressió"));
    }

    @Test (expected = ExceptionExpressionExists.class)
    public void testCreateExpressionException() throws ExceptionExpressionExists {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.createExpression("Segona expressió");
        System.out.println("Això no apareix per pantalla");
    }

    @Test
    public void testDeleteExpressionCorrect() throws ExceptionNoExpression {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.deleteExpression("Segona expressió");
        Map<String, Expression> returnValue = es.getExpressions();
        assertFalse(returnValue.containsKey("Segona expressió"));
    }

    @Test
    public void testDeleteExpressionAll() throws ExceptionNoExpression {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.deleteExpression("Primera expressió");
        es.deleteExpression("Segona expressió");
        es.deleteExpression("Tercera i última expressió");
        Map<String, Expression> returnValue = es.getExpressions();
        assertTrue(returnValue.isEmpty());
    }

    @Test (expected = ExceptionNoExpression.class)
    public void testDeleteExpressionException() throws ExceptionNoExpression {
        ExpressionsSet es = ExpressionsSet.getInstance();
        es.setExpressions(testValues);
        es.deleteExpression("Expressió inventada");
        System.out.println("Això no apareix per pantalla");
    }
}
