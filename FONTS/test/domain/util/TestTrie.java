package test.domain.util;

import main.domain.util.Trie;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @class TestTrie
 * @brief Classe per provar de forma unitària la classe Trie
 * @author marc.valls.camps
 */
public class TestTrie {
    @Test
    public void testTrie1() {
        Trie t = new Trie();
        t.insert("marc");
        assertTrue(t.contains("marc"));
    }
}
