package test.domain.documents;

import java.util.*;
import main.domain.documents.Document;

//TestChecks
import static org.junit.Assert.*;

import main.excepcions.ExceptionInvalidFormat;
import org.junit.Before;
import org.junit.Test;

/**
 * @class TestDocument
 * @brief Classe per provar de forma unitària la classe Document
 * @author ariadna.cortes.danes
 */
public class TestDocument {
    private static  Map<String,Integer> presence;
    private static int num_docs;


    @Before 
    public void init(){ 
        num_docs = 10;
        presence = new HashMap<String,Integer>();
        presence.put("tres", 7);
        presence.put("tristes", 2);
        presence.put("tigres", 1);
        presence.put("comen", 3);
        presence.put("trigo", 2);
        presence.put("en", 6);
        presence.put("un", 9);
        presence.put("trigal", 1);
        presence.put("test", 10);
        presence.put("mesTest", 9);
        presence.put("paraulaaa", 9);
        presence.put("parauula", 9);
    }

    /**
	 * Test dels getters
	 */
    @Test
	public void testGetters() throws ExceptionInvalidFormat {
        //test els getters per als diferents tipus de constructores
        Document doc1 = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal.", "txt");
        assertEquals("Ari", doc1.getAuthor());
        assertEquals("Titol del document", doc1.getTitle());
        assertEquals("Tres tristes, tristes tigres comen trigo en un trigal.", doc1.getContent());
        assertEquals("txt", doc1.getOriginalFormat());

        //Test dels getters de una creadora sense format
        Document doc2 = new Document("Ari", "Titol del document", "Contingut");
        assertEquals("Ari", doc2.getAuthor());
        assertEquals("Titol del document", doc2.getTitle());
        assertEquals("Contingut", doc2.getContent());
        assertEquals(null, doc2.getOriginalFormat());
    }

    /**
	 * Test dels setters
	 */
    @Test
	public void testSetters() throws ExceptionInvalidFormat {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal.", "txt");
        doc.setContent("Now content :)");
        assertEquals("Now content :)", doc.getContent());
    }

    @Test (expected = ExceptionInvalidFormat.class)
    public void testInvalidFormatException() throws ExceptionInvalidFormat {
        Document doc2 = new Document("Ari", "Titol del document", "Contingut", "invalid");
        //Salta ExceptionInvalidFormat
        System.out.println("Això no apareix per pantalla");
    }

    /**
	 * Test dels setters
	 */
    @Test
	public void testTermRelevance_tf_idf() throws ExceptionInvalidFormat {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal.", "txt");
        String[] words = {"tigres","trigo"};

        assertEquals(0.1887, doc.termRelevance_tf_idf(words,num_docs,presence),0.01);
    }
    
    /**
	 * Test dels setters
	 */
    @Test
	public void testQueryRelevance() throws ExceptionInvalidFormat {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal", "txt");
        String query = "deportes de equipo";
        assertEquals(0.0, doc.queryRelevance(query,num_docs,presence),0.1);
    }

    @Test
	public void testCompare() throws ExceptionInvalidFormat {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal", "txt");
        String query = "deportes de equipo";
        assertEquals(0.0, doc.queryRelevance(query,num_docs,presence),0.1);
    }
}
