package test.domain;

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
                            testListAllDocuments();
                            break;
                        case 7:
                            testListSimilars();
                            break;
                        case 8:
                            testListTitlesOfAuthor();
                            break;
                        case 9:
                            testListAuthorsByPrefix();
                            break;
                        case 10:
                            testListByQuery();
                            break;
                        case 11:
                            testListByExpression();
                            break;
                        case 12:
                            testCreateExpression();
                            break;
                        case 13:
                            testDeleteExpression();
                            break;
                        case 14:
                            testModifyExpression();
                            break;
                        case 15:
                            break;
                        default:
                            System.out.println("No has introduït un valor vàlid.");
                            break;
                    }
                } catch(Exception e) {
                    String missatge = e.getMessage();
                    System.out.println(missatge);
                }
                System.out.println();
            } while (value != 14);
            System.out.println("Sortim.");
        } catch (Exception e) {
            System.out.println("El valor introduït no es pot processar, abortem.");
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
        System.out.println("6 - Llistar tots els documents");
        System.out.println("7 - Llistar els documents més semblants a un document");
        System.out.println("8 - Llistar els títols d'un autor");
        System.out.println("9 - Llistar els autors per prefix");
        System.out.println("10 - Llistar els documents a partir d'una query");
        System.out.println("11 - Llistar els documents a partir d'una expressió booleana");
        System.out.println("12 - Crear una expressió booleana");
        System.out.println("13 - Esborrar una expressió booleana");
        System.out.println("14 - Modificar una expressió booleana");
        System.out.println("15 - Sortir :)");
        System.out.print("Quina operació vols realitzar?: ");
    }

    public static void testCreateEmptyDocument() throws ExceptionDocumentExists {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Crear un document buit");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        cd.createEmptyDocument(title, author);
        System.out.println("Document buit creat");
    }

    public static void testDeleteDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Esborrar un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        cd.deleteDocument(title, author);
        System.out.println("Document esborrat");
    }

    public static void testExistsDocument() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saber si existeix un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        if (cd.existsDocument(title, author)) System.out.println("Sí que existeix el document");
        else System.out.println("No existeix el document");
    }

    public static void testGetContentDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Consultar el contingut d'un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        String content = cd.getContentDocument(title, author);
        if ("".equals(content)) System.out.println("El contingut del document és buit");
        else System.out.println(content);
    }

    public static void testUpdateContentDocument() throws ExceptionNoDocument {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Modificar el contingut d'un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Introdueix el nou contingut: ");
        String content = scanner.nextLine();
        cd.updateContentDocument(title, author, content);
        System.out.println("Contingut modificat");
    }

    public static void testListAllDocuments() {
        List<Pair<String, String>> result = cd.listAllDocuments();
        if (result == null || result.size() == 0) System.out.println("No hi ha cap document");
        for (Pair<String, String> r : result) {
            System.out.println(r.getFirst() + " " + r.getSecond());
        }
    }

    public static void testListSimilars() throws ExceptionNoDocument, ExceptionInvalidStrategy {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents més semblants a un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.nextLine();
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        System.out.print("Quina estratègia vols usar (tf-idf/tf-boolean): ");
        String strategy = scanner.nextLine();
        System.out.print("Introdueix una k: ");
        int k = scanner.nextInt();
        List<Pair<String, String>> result = cd.listSimilars(title, author, k, strategy);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        for (Pair<String, String> r : result) {
            System.out.println(r.getFirst() + " " + r.getSecond());
        }
    }

    public static void testListTitlesOfAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els títols d'un autor");
        System.out.print("Introdueix un autor: ");
        String author = scanner.nextLine();
        List<Pair<String, String>> result = cd.listTitlesOfAuthor(author);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        for (Pair<String, String> r : result) {
            System.out.println(r.getFirst() + " " + r.getSecond());
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

    public static void testListByQuery() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents a partir d'una query");
        System.out.print("Introdueix una query: ");
        String query = scanner.nextLine();
        System.out.print("Introdueix una k: ");
        int k = scanner.nextInt();
        List<Pair<String, String>> result = cd.listByQuery(query, k);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        else {
            for (Pair<String, String> r : result) {
                System.out.println(r.getFirst() + " " + r.getSecond());
            }
        }
    }

    public static void testListByExpression() throws ExceptionNoExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Llistar els documents a partir d'una expressió booleana");
        System.out.print("Introdueix una expressió booleana: ");
        String expression = scanner.nextLine();
        System.out.print("Vols que sigui case sensitive (S/N): ");
        Boolean caseSensitive = scanner.nextLine().equals("S");
        List<Pair<String, String>> result = cd.listByExpression(expression, caseSensitive);
        if (result == null || result.size() == 0) System.out.println("No hi ha resultats");
        for (Pair<String, String> r : result) {
            System.out.println(r.getFirst() + " " + r.getSecond());
        }
    }

    public static void testCreateExpression() throws ExceptionExpressionExists, ExceptionInvalidExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Crear una expressió booleana");
        System.out.print("Introdueix una expressió booleana: ");
        String expression = scanner.nextLine();
        cd.createExpression(expression);
        System.out.println("Expressió booleana creada");
    }

    public static void testDeleteExpression() throws ExceptionNoExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Esborrar una expressió booleana");
        System.out.print("Introdueix una expressió booleana: ");
        String expression = scanner.nextLine();
        cd.deleteExpression(expression);
        System.out.println("Expressió booleana esborrada");
    }

    public static void testModifyExpression() throws ExceptionNoExpression, ExceptionExpressionExists, ExceptionInvalidExpression {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Modificar una expressió booleana");
        System.out.print("Introdueix l'expressió booleana a modificar: ");
        String oldExpression = scanner.nextLine();
        System.out.print("Introdueix l'expressió booleana modificada: ");
        String newExpression = scanner.nextLine();
        cd.modifyExpression(oldExpression, newExpression);
        System.out.println("Expressió booleana modificada");
    }
}
