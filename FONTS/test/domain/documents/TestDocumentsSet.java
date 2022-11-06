package test.domain.documents;

import main.domain.documents.DocumentsSet;
import test.domain.expressions.Expression;
import main.domain.util.Pair;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionNoDocument;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @class TestDocumentsSet
 * @brief Classe per provar de forma unitària la classe DocumentsSet
 * @author marc.lopez.domenech i pau.duran.manzano
 * @note Tal com hem implementat l'stub de Document per fer proves, el contingut de tots els documents ha de ser de lletres soltes, és a dir, un contingut pot ser "h o l a"
 */
public class TestDocumentsSet {
    private static Map<String, Map<String, Document>> testValues;

    @Before
    public void ini() {
        testValues = new HashMap<>();

        Map<String, Document> titleDoc1 = new HashMap<>();
        titleDoc1.put("t1", new Document("t1", "a1", "c 1"));
        testValues.put("a1", titleDoc1);

        Map<String, Document> titleDoc2 = new HashMap<>();
        titleDoc1.put("t2", new Document("t2", "a2", "c 2 2 2 2 2 2 2"));
        testValues.put("a2", titleDoc2);

        // Igual autor que l'anterior
        Map<String, Document> titleDoc3 = new HashMap<>();
        titleDoc1.put("t2", new Document("t3", "a2", "c 2 3 2 3"));
        testValues.put("a2", titleDoc3);
    }

    @Test
    public void testGetInstance() {
        DocumentsSet ds1 = DocumentsSet.getInstance();
        DocumentsSet ds2 = DocumentsSet.getInstance();
        assertSame(ds1, ds2);
    }

    @Test
    public void testGettersISetters() {
        DocumentsSet ds = DocumentsSet.getInstance();
        Document d1 = new Document("t1", "a1", "c 1");
        ds.setDocuments();
    }

    @Test
    public void createDocument() {
    }

    @Test
    public void testDeleteDocument() {
    }

    @Test
    public void testExistsDocument() {
    }

    @Test
    public void testGetContentDocument() {
    }

    @Test
    public void testUpdateContentDocument() {
    }

    @Test
    public void testListSimilars() {
    }

    @Test
    public void testListByExpression() {
    }

    @Test
    public void testListTitlesOfAuthor() {
    }

    @Test
    public void testListAuthorsByPrefix() {
    }

    @Test
    public void testListByQuery() {
    }
}
