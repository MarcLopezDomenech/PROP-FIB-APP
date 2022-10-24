package domain.documents;

import java.util.*;

//TestChecks
import static org.junit.Assert.*;
import org.junit.Test;

import domain.documents.Document;

public class TestDocument {
    
    /**
	 * Test dels getters
	 */
    @Test
	public void GetterTesting() throws Exception
	{
        Document doc = new Document("Ari", "Titol del document", "Aixo es el contingut", "txt");
        assertEquals("Ari", doc.getAuthor());
        assertEquals("Titol del document", doc.getTitle());
        assertEquals("Aixo es el contingut", doc.getContent());
        assertEquals("txt", doc.getOriginalFormat());

        //getter del internal document :)

    }

    /**
	 * Test dels setters
	 */
    @Test
	public void SetterTesting() throws Exception
	{
        Document doc = new Document("Ari", "Titol del document", "Aixo es el contingut", "txt");
        doc.setContent("Now content :)");
        assertEquals("Now content :)", doc.getContent());
    }

    /**
	 * Test dels setters
	 */
    @Test
	public void queryRelevanceTesting() throws Exception
	{
        Document doc = new Document("Ari", "Titol del document", "furbo furbo unga unga", "txt");
        String query = "me gusta el furbo";
        assertEquals(25, doc.queryRelevance(query));
    }
}
