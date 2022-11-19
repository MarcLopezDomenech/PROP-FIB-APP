package test.domain.util;

import main.domain.util.Trie;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @class TestTrie
 * @brief Classe per provar de forma unitària la classe Trie
 * @author marc.valls.camps
 */
public class TestTrie {
    @Test
    public void testInsertRemoveBoolean() {
        Trie t = new Trie();
        assertTrue(t.insert("marc"));
        assertFalse(t.insert("marc"));

        assertTrue(t.contains("marc"));

        assertTrue(t.remove("marc"));
        assertFalse(t.remove("marc"));

        assertFalse(t.contains("marc"));
    }

    @Test
    public void testPrefixes() {
        Trie t = new Trie();
        assertTrue(t.insert("marc"));
        assertTrue(t.insert("marta"));
        assertTrue(t.insert("marcel"));

        assertTrue(t.contains("marc"));
        assertTrue(t.contains("marta"));
        assertTrue(t.contains("marcel"));

        ArrayList<String> result = new ArrayList<>(Arrays.asList("marc", "marcel"));
        assertEquals(result,t.wordsGivenPrefix("marc"));

        result.add("marta");
        assertEquals(result,t.wordsGivenPrefix("mar"));
        assertEquals(result,t.wordsGivenPrefix(""));

        result.remove("marc");
        assertTrue(t.remove("marc"));
        assertEquals(result,t.wordsGivenPrefix("mar"));

        result.remove("marta");
        assertEquals(result,t.wordsGivenPrefix("marc"));
    }
}
