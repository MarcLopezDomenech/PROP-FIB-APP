package test.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

import main.domain.util.Pair;
import main.domain.CtrlDomain;
import main.excepcions.*;

/**
 * @class DriverCtrlDomain
 * @brief Classe per fer test d'integració de tot el domini
 * @author pau.duran.manzano
 */
public class DriverCtrlDomain {
    /**
     * \brief Instància del controlador de domini del sistema (CtrlDomain)
     */
    private static CtrlDomain cd;

    public static void main(String[] args) {
        cd = CtrlDomain.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Benvingut/da al Driver del Controlador de Domini");
        try {
            int value;
            do {
                imprimirOpcions();
                value = scanner.nextInt();
                System.out.println();
                try {
                    switch (value) {
                        case 1:
                            testCreateEmptyDocument();
                            break;
                        case 2:
                            testDeleteDocument();
                            break;
                        case 3:
                            testExistsDocument();
                            break;
                        case 4:
                            testGetContentDocument();
                            break;
                        case 5:
                            testUpdateContentDocument();
                            break;
                        case 6:
                            testGetLanguageDocument();
                            break;
                        case 7:
                            testUpdateLanguageDocument();
                            break;
                        case 8:
                            testListAllDocuments();
                            break;
                        case 9:
                            testListSimilars();
                            break;
                        case 10:
                            testListTitlesOfAuthor();
                            break;
                        case 11:
                            testListAuthorsByPrefix();
                            break;
                        case 12:
                            testListByQuery();
                            break;
                        case 13:
                            testListByExpression();
                            break;
                        case 14:
                            testCreateExpression();
                            break;
                        case 15:
                            testDeleteExpression();
                            break;
                        case 16:
                            testModifyExpression();
                            break;
                        case 17:
                            break;
                        case 18:
                            testImportDocument();
                            break;
                        case 19:
                            testExportDocument();
                            break;
                        default:
                            System.out.println("No has introduit un valor valid.");
                            break;
                    }
                } catch(Exception e) {
                    String missatge = e.getMessage();
                    System.out.println(missatge);
                }
                System.out.println();
            } while (value != 17);
            System.out.println("Sortim.");
        } catch (Exception e) {
            System.out.println("El valor introduit no es pot processar, abortem.");
            return;
        }
    }

    public static void imprimirOpcions() {
        System.out.println("Seguit de funcions disponibles al Driver");
        System.out.println("1 - Crear un document buit");
        System.out.println("2 - Esborrar un document");
        System.out.println("3 - Saber si existeix un document");
        System.out.println("4 - Consultar el contingut d'un document");
        System.out.println("5 - Modificar el contingut d'un document");
        System.out.println("6 - Consultar l'idioma d'un document");
        System.out.println("7 - Modificar l'idioma d'un document");
        System.out.println("8 - Llistar tots els documents");
        System.out.println("9 - Llistar els documents mes semblants a un document");
        System.out.println("10 - Llistar els titols d'un autor");
        System.out.println("11 - Llistar els autors per prefix");
        System.out.println("12 - Llistar els documents a partir d'una query");
        System.out.println("13 - Llistar els documents a partir d'una expressio booleana");
        System.out.println("14 - Crear una expressio booleana");
        System.out.println("15 - Esborrar una expressio booleana");
        System.out.println("16 - Modificar una expressio booleana"); 
        System.out.println("17 - Sortir :)");
        System.out.println("18- Carregar document");
        System.out.println("19- Exportar document");
        System.out.print("Quina operacio vols realitzar?: ");
    }

    public static void testCreateEmptyDocument() throws ExceptionDocumentExists, ExceptionInvalidLanguage {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Crear un document buit");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Introdueix un idioma(ca/en/es): ");
        String language = scanner.nextLine();
        cd.createEmptyDocument(title, author, language);
        System.out.println("Document buit creat");
    }

    public static void testDeleteDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Esborrar un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        cd.deleteDocument(title, author);
        System.out.println("Document esborrat");
    }

    public static void testExistsDocument() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saber si existeix un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        if (cd.existsDocument(title, author)) System.out.println("Si que existeix el document");
        else System.out.println("No existeix el document");
    }

    public static void testGetContentDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Consultar el contingut d'un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        String content = cd.getContentDocument(title, author);
        if ("".equals(content)) System.out.println("El contingut del document es buit");
        else System.out.println(content);
    }

    public static void testUpdateContentDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Modificar el contingut d'un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Introdueix el nou contingut: ");
        String content = scanner.nextLine();
        System.out.print("Vols guardar? (S/N): ");
        Boolean save = scanner.nextLine().equals("S");
        if (save) {
            cd.updateContentDocument(title, author, content);
            System.out.println("Contingut modificat");
        }
        else System.out.println("S'ha sortit sense guardar");
    }

    public static void testGetLanguageDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Consultar l'idioma d'un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        String language = cd.getLanguageDocument(title, author);
        if ("ca".equals(language)) System.out.println("Catala (ca)");
        else if ("en".equals(language)) System.out.println("Angles (en)");
        else System.out.println("Castella (es)");
    }

    public static void testUpdateLanguageDocument() throws ExceptionNoDocument, ExceptionInvalidLanguage {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Modificar l'idioma d'un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Introdueix el nou idioma(ca/en/es): ");
        String language = scanner.nextLine();
        cd.updateLanguageDocument(title, author, language);
        System.out.println("Idioma modificat");
    }

    public static void testListAllDocuments() {
        List<Object[]> result = cd.listAllDocuments();
        if (result == null || result.size() == 0) System.out.println("No hi ha cap document");
        for (Object[] r : result) {
            System.out.println(r[1] + " " + r[2]);
        }
    }

    public static void testListSimilars() throws ExceptionNoDocument, ExceptionInvalidStrategy, ExceptionInvalidK {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents mes semblants a un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Quina estrategia vols usar (tf-idf/tf-boolean): ");
        String strategy = scanner.nextLine();
        System.out.print("Introdueix una k: ");
        try {
            int k = scanner.nextInt();
            List<Object[]> result = cd.listSimilars(title, author, k, strategy);
            if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
            for (Object[] r : result) {
                System.out.println(r[1] + " " + r[2]);
            }
        } catch (java.util.InputMismatchException e) {        // Excepció que llença scanner si no es rep un int
            throw new ExceptionInvalidK();
        }
    }

    public static void testListTitlesOfAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els titols d'un autor");
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        List<Object[]> result = cd.listTitlesOfAuthor(author);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        for (Object[] r : result) {
            System.out.println(r[1] + " " + r[2]);
        }
    }

    public static void testListAuthorsByPrefix() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els autors per prefix");
        System.out.print("Introdueix un prefix: ");
        String prefix = scanner.nextLine();
        List<String> authors = cd.listAuthorsByPrefix(prefix);
        if (authors == null || authors.size() == 0) System.out.println("No hi ha resultats");
        for (String a : authors) {
            System.out.println(a);
        }
    }

    public static void testListByQuery() throws ExceptionInvalidK {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents a partir d'una query");
        System.out.print("Introdueix una query: ");
        String query = scanner.nextLine();
        System.out.print("Introdueix una k: ");
        try {
            int k = scanner.nextInt();
            if (k < 0);
            List<Object[]> result = cd.listByQuery(query, k);
            if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
            else {
                for (Object[] r : result) {
                    System.out.println(r[1] + " " + r[2]);
                }
            }
        } catch (java.util.InputMismatchException e) {        // Excepció que llença scanner si no es rep un int
            throw new ExceptionInvalidK();
        }
    }

    public static void testListByExpression() throws ExceptionNoExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents a partir d'una expressio booleana");
        System.out.print("Introdueix una expressio booleana: ");
        String expression = scanner.nextLine();
        System.out.print("Vols que sigui case sensitive (S/N): ");
        Boolean caseSensitive = scanner.nextLine().equals("S");
        List<Object[]> result = cd.listByExpression(expression, caseSensitive);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        for (Object[] r : result) {
            System.out.println(r[1] + " " + r[2]);
        }
    }

    public static void testCreateExpression() throws ExceptionExpressionExists, ExceptionInvalidExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Crear una expressio booleana");
        System.out.print("Introdueix una expressio booleana: ");
        String expression = scanner.nextLine();
        cd.createExpression(expression);
        System.out.println("Expressio booleana creada");
    }

    public static void testDeleteExpression() throws ExceptionNoExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Esborrar una expressio booleana");
        System.out.print("Introdueix una expressio booleana: ");
        String expression = scanner.nextLine();
        cd.deleteExpression(expression);
        System.out.println("Expressio booleana esborrada");
    }

    public static void testModifyExpression() throws ExceptionNoExpression, ExceptionExpressionExists, ExceptionInvalidExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Modificar una expressio booleana");
        System.out.print("Introdueix l'expressio booleana a modificar: ");
        String oldExpression = scanner.nextLine();
        System.out.print("Introdueix l'expressio booleana modificada: ");
        String newExpression = scanner.nextLine();
        cd.modifyExpression(oldExpression, newExpression);
        System.out.println("Expressio booleana modificada");
    }

    public static void testImportDocument() throws ExceptionInvalidFormat, FileNotFoundException, ExceptionDocumentExists, ExceptionInvalidLanguage, ExceptionInvalidCharacter, ExceptionMissingTitleOrAuthor {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Importar un document");
        System.out.println("Introdueix el path");
        String path = scanner.nextLine();
        System.out.println("Introdueix l'idioma");
        String language = scanner.nextLine();
        cd.importDocument(path, language);
        System.out.println("Document importat");
    }

    public static void testExportDocument() throws ExceptionNoDocument, IOException, ExceptionInvalidFormat {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Exportar un document");
        System.out.print("Introdueix un titol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.println("Introdueix el path");
        String path = scanner.nextLine();
        cd.exportDocument(title,author,path);
        System.out.println("Document exportat");
    }
}
