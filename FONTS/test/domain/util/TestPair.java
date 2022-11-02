package test.domain.util;

import main.domain.util.Pair;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @class TestPair
 * @brief Classe per provar de forma unitària la classe Pair
 * @author pau.duran.manzano
 */
public class TestPair {

    @Test
    public void testGettersString() {
        Pair<String, String> p = new Pair("autor", "títol");
        assertEquals("autor", p.getFirst());
        assertEquals("títol", p.getSecond());
    }

    @Test
    public void testSettersString() {
        Pair<String, String> p = new Pair<>();
        p.setFirst("first");
        assertEquals("first", p.getFirst());
        p.setSecond("second");
        assertEquals("second", p.getSecond());
    }

    @Test
    public void testSetters2String() {
        Pair<String, String> p = new Pair<>("not first", "not second");
        p.setFirst("first");
        assertEquals("first", p.getFirst());
        p.setSecond("second");
        assertEquals("second", p.getSecond());
    }

    @Test
    public void testGettersDouble() {
        Pair<Double, Double> p = new Pair(1.0, 2.456);
        assertEquals(1.0, p.getFirst(), 0);
        assertEquals(2.456, p.getSecond(), 0);
    }

    @Test
    public void testSettersDouble() {
        Pair<Double, Double> p = new Pair<>();
        p.setFirst(1.0);
        assertEquals(1.0, p.getFirst(), 0);
        p.setSecond(2.456);
        assertEquals(2.456, p.getSecond(), 0);
    }

    @Test
    public void testSetters2Double() {
        Pair<Double, Double> p = new Pair<>(11.23, 13.24);
        p.setFirst(1.0);
        assertEquals(1.0, p.getFirst(), 0);
        p.setSecond(2.456);
        assertEquals(2.456, p.getSecond(), 0);
    }
}
