package test.domain.documents;

import main.domain.documents.DocumentsSet;
import main.domain.expressions.Expression;  // Canviar per per test.domain.expressions.Expression fer tests
import main.domain.util.Pair;
import main.domain.documents.Document;      // Canviar per test.domain.documents.Document per fer tests

import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionNoDocument;
import main.excepcions.ExceptionInvalidLanguage;
import main.excepcions.ExceptionInvalidK;
import main.excepcions.ExceptionInvalidStrategy;
import main.excepcions.ExceptionInvalidExpression;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @class TestDocumentsSet
 * @brief Classe per provar de forma unitària la classe DocumentsSet
 * @author marc.lopez.domenech
 * @note Tal com hem implementat l'stub de Document per fer proves, el contingut de tots els documents ha de ser de lletres soltes, és a dir, un contingut pot ser "h o l a"
 */
public class TestDocumentsSet {
    private static Map<String, Map<String, Document>> testValues;

    @Before
    public void ini() throws ExceptionInvalidLanguage {
        testValues = new HashMap<>();

        Map<String, Document> titleDoc1 = new HashMap<>();
        titleDoc1.put("t1", new Document("t1", "a1", "c 1", "ca"));
        testValues.put("a1", titleDoc1);

        Map<String, Document> titleDoc2 = new HashMap<>();
        titleDoc2.put("t2", new Document("t2", "a2", "c 2 2 2 2 2 2 2", "ca"));
        testValues.put("a2", titleDoc2);

        // Igual autor que l'anterior
        titleDoc2.put("t3", new Document("t3", "a2", "c 2 3 2 3", "ca"));
        testValues.put("a2", titleDoc2);
    }

    @Test
    public void testGetInstance() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        DocumentsSet ds2 = DocumentsSet.getInstance();
        assertSame(ds1, ds2);
    }

    @Test
    public void testGettersISetters() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);
        assertSame(testValues,ds1.getDocuments());
    }
    @Test
    public void testgetnumDocuments() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        assertEquals(3, ds1.getnumDocuments());
    }
    @Test
    public void testgetpresence() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        Map<String, Integer> result = new HashMap<>();
        result.put("c", 3);
        result.put("1", 1);
        result.put("2", 2);
        result.put("3", 1);
        assertEquals(result, ds1.getpresence());
    }   
    @Test
    public void createDocument() throws ExceptionDocumentExists , ExceptionInvalidLanguage,ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.createDocument("t1","a3","a f g g h","ca");
        assertEquals(true, ds1.existsDocument("t1","a3"));
        assertEquals("a f g g h", ds1.getContentDocument("t1","a3"));
        assertEquals(4, ds1.getnumDocuments());

        Map<String, Integer> result = new HashMap<>();
        result.put("c", 3);
        result.put("1", 1);
        result.put("2", 2);
        result.put("3", 1);
        result.put("a", 1);
        result.put("f", 1);
        result.put("g", 1);
        result.put("h", 1);
        assertEquals(result, ds1.getpresence());
        //presence
    }
    @Test (expected = ExceptionDocumentExists.class)
    public void createDocumentException1() throws ExceptionDocumentExists , ExceptionInvalidLanguage{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.createDocument("t2","a2","acccf4 5","cat");
        System.out.println("Això no apareix per pantalla");
    }

    @Test (expected = ExceptionInvalidLanguage.class)
    public void createDocumentException2() throws ExceptionDocumentExists , ExceptionInvalidLanguage{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.createDocument("t5","a1","acccf4 5","html");
        System.out.println("Això no apareix per pantalla");
    }

    @Test
    public void testDeleteDocumentCorrect() throws ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.deleteDocument("t1","a1");
        assertFalse(ds1.existsDocument("t1","a1"));
        assertEquals(2, ds1.getnumDocuments());
        Map<String, Integer> result = new HashMap<>();
        result.put("c", 2);
        result.put("2", 2);
        result.put("3", 1);
        assertEquals(result, ds1.getpresence());
        
    }
    @Test (expected = ExceptionNoDocument.class)
    public void testDeleteDocumentException() throws ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.deleteDocument("t1","a3");
        System.out.println("Això no apareix per pantalla");   
    }
    @Test
    public void testExistsDocument() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        assertEquals(true, ds1.existsDocument("t1","a1"));
        assertEquals(testValues, ds1.getDocuments());

        assertEquals(false, ds1.existsDocument("t1","a2"));
        assertEquals(testValues, ds1.getDocuments());
    }

    @Test
    public void testGetContentDocument() throws ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        assertEquals("c 2 2 2 2 2 2 2", ds1.getContentDocument("t2","a2"));
        assertEquals(testValues, ds1.getDocuments());
    }

    @Test
    public void testUpdateContentDocument() throws ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.updateContentDocument("t2","a2","final 2");
        assertEquals("final 2", ds1.getContentDocument("t2","a2"));
    }

    @Test
    public void testListSimilarsCorrect() throws ExceptionInvalidK, ExceptionInvalidStrategy, ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> expected = new ArrayList<>();
        expected.add(new Pair<>("t2","a2"));
        expected.add(new Pair<>("t3","a2"));
        List<Pair<String, String>> result = ds1.listSimilars("t1", "a1", 2, "tf-idf");
        for (int i = 0; i < result.size(); ++i) {
            assertEquals(result.get(i).getFirst(), expected.get(i).getFirst());
            assertEquals(result.get(i).getSecond(), expected.get(i).getSecond());
        }

        assertEquals(testValues, ds1.getDocuments());
    }
    @Test (expected = ExceptionNoDocument.class)
    public void testListSimilarsException1() throws ExceptionInvalidK, ExceptionInvalidStrategy, ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result = new ArrayList<>();
        result =ds1.listSimilars("t1", "a3", 0, "tf-idf");
        System.out.println("Això no apareix per pantalla");
    }
    @Test (expected = ExceptionInvalidStrategy.class)
    public void testListSimilarsException2() throws ExceptionInvalidK, ExceptionInvalidStrategy, ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result = new ArrayList<>();
        result =ds1.listSimilars("t1", "a1", 0, "algo");
        System.out.println("Això no apareix per pantalla");
    }
    @Test (expected = ExceptionInvalidK.class)
    public void testListSimilarsException3() throws ExceptionInvalidK, ExceptionInvalidStrategy, ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result = new ArrayList<>();
        result=ds1.listSimilars("t1", "a1", -5, "tf-idf");
        System.out.println("Això no apareix per pantalla");
    }
    @Test (expected = ExceptionNoDocument.class)
    public void testListSimilarsException4() throws ExceptionInvalidK, ExceptionInvalidStrategy, ExceptionNoDocument{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result = new ArrayList<>();
        result=ds1.listSimilars("t4", "a6", 2, "tf-idf");
        System.out.println("Això no apareix per pantalla");
    }
    @Test
    public void testListByExpression() throws ExceptionInvalidExpression{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result= new ArrayList<Pair<String, String>>();
        result.add(new Pair<>("t2","a2"));
        result.add(new Pair<>("t3","a2"));
        Expression a=Expression.create("hola");
        Boolean b=true;
        List<Pair<String, String>> expected = ds1.listByExpression(a, b);
        for (int i = 0; i < result.size(); ++i) {
            assertEquals(result.get(i).getFirst(), expected.get(i).getFirst());
            assertEquals(result.get(i).getSecond(), expected.get(i).getSecond());
        }

        assertEquals(testValues, ds1.getDocuments());

    }
    @Test
    public void testListTitlesOfAuthor() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        List<Pair<String, String>> result = new ArrayList<Pair<String,String>>(); 
        result.add(new Pair<String, String>("t2", "a2"));
        result.add(new Pair<String, String>("t3", "a2"));

        List<Pair<String, String>> expected = ds1.listTitlesOfAuthor("a2");

        for (int i = 0; i < result.size(); ++i) {
            assertEquals(result.get(i).getFirst(), expected.get(i).getFirst());
            assertEquals(result.get(i).getSecond(), expected.get(i).getSecond());
        }
        
        assertEquals(testValues, ds1.getDocuments());

        List<Pair<String, String>> result1 = new ArrayList<Pair<String,String>>();  
        
        assertEquals(result1, ds1.listTitlesOfAuthor("a3"));
        assertEquals(testValues, ds1.getDocuments());
    }

    @Test
    public void testListAuthorsByPrefix() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ArrayList<String> result = new ArrayList<String>();
        result.add("a1");
        result.add("a2");

        List<String> expected = ds1.listAuthorsByPrefix("");

        for (int i = 0; i < result.size(); ++i) {
            assertEquals(expected.get(i), result.get(i));
        }

        assertEquals(testValues, ds1.getDocuments());

        ArrayList<String> result1 = new ArrayList<String>();

        assertEquals(result1, ds1.listAuthorsByPrefix("a3"));
        assertEquals(testValues, ds1.getDocuments());

        assertEquals(result1, ds1.listAuthorsByPrefix("a325h"));
        assertEquals(testValues, ds1.getDocuments());
    }

    @Test (expected = ExceptionInvalidK.class)
    public void testListByQueryException() throws ExceptionInvalidK{
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);

        ds1.listByQuery("hola", -1);
        System.out.println("Això no apareix per pantalla");
    }
    @Test
    public void testListByQueryCorrect() throws ExceptionInvalidK {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        ds1.setDocuments(testValues);
        
        List<Pair<String, String>> result= new ArrayList<Pair<String, String>>();
        result.add(new Pair<>("t2","a2"));
        result.add(new Pair<>("t3","a2"));
        result.add(new Pair<>("t1","a1"));

        List<Pair<String, String>> expected = ds1.listByQuery("ho",3);

        for (int i = 0; i < result.size(); ++i) {
            assertEquals(result.get(i).getFirst(), expected.get(i).getFirst());
            assertEquals(result.get(i).getSecond(), expected.get(i).getSecond());
        }
        assertEquals(testValues, ds1.getDocuments());
    }
}
