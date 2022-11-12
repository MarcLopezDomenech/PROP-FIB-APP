package test.domain.documents;

import java.util.*;
import main.domain.documents.Document;

//TestChecks
import static org.junit.Assert.*;

import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;

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
        presence.put("Tres", 7);
        presence.put("tristes", 2);
        presence.put("tigres", 1);
        presence.put("comen", 3);
        presence.put("trigo", 2);
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
	public void testGetters() throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        //test els getters per als diferents tipus de constructores
        Document doc1 = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal.", "en","txt");
        assertEquals("Ari", doc1.getAuthor());
        assertEquals("Titol del document", doc1.getTitle());
        assertEquals("en", doc1.getLanguage());
        assertEquals("Tres tristes, tristes tigres comen trigo en un trigal.", doc1.getContent());
        assertEquals("txt", doc1.getOriginalFormat());

        //Test dels getters de una creadora sense format
        Document doc2 = new Document("Ari", "Titol del document", "Contingut", "ca");
        assertEquals("Ari", doc2.getAuthor());
        assertEquals("Titol del document", doc2.getTitle());
        assertEquals("Contingut", doc2.getContent());
        assertEquals("ca", doc2.getLanguage());
        assertEquals(null, doc2.getOriginalFormat());
    }

    /**
	 * Test dels setters
	 */
    @Test
	public void testSetters() throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal.", "en", "txt");
        doc.setContent("Now content :)");
        assertEquals("Now content :)", doc.getContent());

        doc.setLanguage("es");
        assertEquals("es", doc.getLanguage());
    }

    @Test (expected = ExceptionInvalidLanguage.class)
    public void testInvalidLanguageException() throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        Document doc = new Document("Ari","Titol del document", "Contingut", "invalid");
        //Salta ExceptionInvalidLanguage
        System.out.println("Això no apareix per pantalla");
    }

    @Test (expected = ExceptionInvalidLanguage.class)
    public void testInvalidLanguageExceptionSetter() throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        Document doc = new Document("Ari","Titol del document", "Contingut", "en");
        doc.setLanguage("invalid");
        //Salta ExceptionInvalidLanguage
        System.out.println("Això no apareix per pantalla");
    }

    @Test (expected = ExceptionInvalidFormat.class)
    public void testInvalidFormatException() throws ExceptionInvalidFormat, ExceptionInvalidLanguage {
        Document doc = new Document("Ari","Titol del document", "Contingut", "ca", "invalid");
        //Salta ExceptionInvalidFormat
        System.out.println("Això no apareix per pantalla");
    }
    
    @Test
	public void testQueryRelevance() throws ExceptionInvalidLanguage {
        Document doc = new Document("Ari", "t1", "Tres tristes, tristes tigres comen trigo en un trigal", "es");
        String query = "Mi mama me mima";
        assertEquals(0.0, doc.queryRelevance(query,num_docs,presence),0.01);

        String query2 = "tigres trigo";
        assertEquals(24.27, doc.queryRelevance(query2,num_docs,presence),0.01);

        String query3 = "Tres trigal en un tigre triste";
        assertEquals(16.5, doc.queryRelevance(query3,num_docs,presence),0.01);
    }

    @Test
	public void testCompare_tf_idf() throws ExceptionInvalidLanguage {
        Document doc = new Document("Ari", "Titol del document", "Tres tristes, tristes tigres comen trigo en un trigal","es");

        Document doc2 = new Document("Ari", "t2", "Mi mama me mima","es");
        assertEquals(0.0, doc.compare_tf_idf(doc2,num_docs,presence),0.1);

        Document doc3 = new Document("Ari", "t3", "Tres tristes palabras","es");
        assertEquals(25.12, doc.compare_tf_idf(doc3,num_docs,presence),0.01);

        Document doc4 = new Document("Ari", "t4", "Tres trigal en un tigre triste","es");
        assertEquals(21.82, doc.compare_tf_idf(doc4,num_docs,presence),0.01);

        //Prova per a documents en diferents idiomes
        Document doc5 = new Document("Ari", "t5", "A cup of cafe con leche in Plaza Mayor", "en");
        assertEquals(0, doc.compare_tf_idf(doc5,num_docs,presence),0.01);
    }

    @Test
	public void testCompare_tf_boolean() throws ExceptionInvalidLanguage {
        Document doc = new Document("Ari", "t1", "Tres tristes, tristes tigres comen trigo en un trigal", "es");

        Document doc2 = new Document("Ari", "t2", "Mi mama me mima", "es");
        assertEquals(0.0, doc.compare_tf_boolean(doc2),0.1);

        Document doc3 = new Document("Ari", "t3", "Tres tristes palabras","es");
        assertEquals(2.0, doc.compare_tf_boolean(doc3),0.01);

        //Si posem stopWords no afecta
        Document doc4 = new Document("Ari", "Tt4", "Tres trigal en un tigre triste","es");
        assertEquals(2.0, doc.compare_tf_boolean(doc4),0.01);

        //Prova per a documents en diferents idiomes
        Document doc5 = new Document("Ari", "t5", "A cup of cafe con leche in Plaza Mayor", "en");
        assertEquals(0, doc.compare_tf_idf(doc5,num_docs,presence),0.01);

        Document doc6 = new Document("Ari", "Tt4", "Tres trigal en un tigre triste","en");
        assertEquals(0.0, doc.compare_tf_boolean(doc6),0.01);
    }
}

