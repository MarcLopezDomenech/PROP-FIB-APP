package test.domain.documents;

import main.domain.documents.InternalDocument;
import java.util.*;

//TestChecks
import static org.junit.Assert.*;

import org.junit.Test;
public class TestInternalDocument {

    /**
     * Few tests on the split method
     */
    @Test
    public void testSplit() {
        String c1 = "Tres,tristes, tigres, ";
        String[] s1 = c1.split("[ ,!?.]+");
        String[] expectedResult = {"Tres", "tristes", "tigres"};
        assertArrayEquals(expectedResult, s1);

        String c2 = "Tres!tristes ! tigres! ";
        String[] s2 = c2.split("[- ,!?.]+");
        assertArrayEquals(expectedResult, s2);

        String c3 = "Tres?tristes ? tigres? ";
        String[] s3 = c3.split("[- ,!?.]+");
        assertArrayEquals(expectedResult, s3);

        String c4 = "Tres. -.- tristes?. tigres...";
        String[] s4 = c4.split("[- ,!?.]+");
        assertArrayEquals(expectedResult, s4);
        
        String c5 = "Tres-tristes-tigres?! -";
        String[] s5 = c5.split("[- ,!?.:]+");
        assertArrayEquals(expectedResult, s5);

        
        String c6 = "Tres :tristes:tigres";
        String[] s6 = c6.split("[- ,!?.:]+");
        assertArrayEquals(expectedResult, s6);
    }

    /**
     * @brief Getter testing and content analysis
     */
    @Test
    public void testAnalizeContent() {
        String content = "Tres tristes, tristes tigres comen trigo en un trigal.";
        InternalDocument internal1 = new InternalDocument(content); //calls analize content
        Set<String> set = internal1.getRelevantKeyWords();

        assertEquals(8, set.size());
        assertTrue(set.contains("Tres"));
        assertTrue(set.contains("tristes"));
        assertTrue(set.contains("tigres"));
        assertTrue(set.contains("comen"));
        assertTrue(set.contains("trigo"));
        assertTrue(set.contains("en"));
        assertTrue(set.contains("un"));
        assertTrue(set.contains("trigal"));

        assertEquals(9, internal1.getTotalWords());

        Map<String,Integer> relevantWords = internal1.getRelevantWords();
        assertTrue(1 ==relevantWords.get("Tres"));
        assertTrue(2 ==relevantWords.get("tristes"));
        assertTrue(1 ==relevantWords.get("tigres"));
        assertTrue(1 ==relevantWords.get("comen"));
        assertTrue(1 ==relevantWords.get("trigo"));
        assertTrue(1 ==relevantWords.get("en"));
        assertTrue(1 ==relevantWords.get("un"));
        assertTrue(1 ==relevantWords.get("trigal"));
        //assertEquals(1, relevantWords.get("trigal"));

        String moreComplicatedContent = "Hola! Com estàs? Jo bé, gracies. (adeu) ";

        InternalDocument internal2 = new InternalDocument(moreComplicatedContent);
        Set<String> set2 = internal2.getRelevantKeyWords();

        assertEquals(7, internal2.getTotalWords());
        assertTrue(set2.contains("(adeu)")); //mal

    }
}
