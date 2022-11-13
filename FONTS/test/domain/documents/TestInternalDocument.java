package test.domain.documents;

import main.domain.documents.InternalDocument;
import java.util.*;

//TestChecks
import static org.junit.Assert.*;

import org.junit.Test;
public class TestInternalDocument {

    /**
     * @brief Test del getters
     */
    @Test
    public void testGetters() {
        String content = "Tres tristes, tristes tigres comen trigo en un trigal.";
        InternalDocument internal1 = new InternalDocument(content, "es"); //calls analize content
        
        //Test de getRelevantKeyWords
        Set<String> set = internal1.getRelevantKeyWords();
        assertEquals(5, set.size());
        assertFalse(set.contains("Tres"));
        assertTrue(set.contains("tristes"));
        assertTrue(set.contains("tigres"));
        assertTrue(set.contains("comen"));
        assertTrue(set.contains("trigo"));
        assertTrue(set.contains("trigal"));

        //Test de getTotalWords
        assertEquals(6, internal1.getTotalWords());

        //Test de getRelevantWords
        Map<String,Integer> relevantWords = internal1.getRelevantWords();
        assertTrue(2 == relevantWords.get("tristes"));
        assertTrue(1 == relevantWords.get("tigres"));
        assertTrue(1 == relevantWords.get("comen"));
        assertTrue(1 == relevantWords.get("trigo"));
        assertTrue(1 == relevantWords.get("trigal"));    
    }


    /**
     * @brief Test dels Setters
     */
    @Test
    public void testNewContent() {
        String initial = "Initial useless content";
        InternalDocument intDoc = new InternalDocument(initial, "en");
        intDoc.newContent("Tres tristes, tristes tigres comen trigo en un trigal.", "es");

        //Comprovació que els atributs de internal document han canviat consequentment
        assertEquals(6, intDoc.getTotalWords());
        
        Set<String> set = intDoc.getRelevantKeyWords();
        assertEquals(5, set.size());
        assertFalse(set.contains("Tres"));       //Stop word
        assertTrue(set.contains("tristes"));
        assertTrue(set.contains("tigres"));
        assertTrue(set.contains("comen"));
        assertTrue(set.contains("trigo"));
        assertFalse(set.contains("en"));        //Stop word
        assertFalse(set.contains("un"));        //Stop word
        assertTrue(set.contains("trigal"));
    }
}
