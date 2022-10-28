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
        String[] s5 = c5.split("[- ,!?.]+");
        assertArrayEquals(expectedResult, s5);
    }
    /**
     * @brief Getter testing
     */
    @Test
    public void testGetters() {
        String content = "Tres tristes tigres comen trigo en un trigal.";
        InternalDocument internal1 = new InternalDocument(content);
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

        assertEquals(8, internal1.getTotalWords());
    }
}
