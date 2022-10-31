package test.domain;

import java.util.Scanner;

import main.domain.CtrlDomain;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionNoDocument;

/**
 * @class DriverCtrlDomain
 * @brief Classe per fer test d'integració de tot el domini
 * @author pau.duran.manzano
 */
public class DriverCtrlDomain {
    private static CtrlDomain cd;
    private static Scanner scanner;

    public static void main(String[] args) {
        cd = CtrlDomain.getInstance();
        scanner = new Scanner(System.in);
        System.out.println("Benvingut/da al Driver del Controlador de Domini");
        try {
            String cont = "S";
            do {
                imprimirOpcions();
                int value = scanner.nextInt();
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
                            testListSimilars();
                            break;
                        case 7:
                            testListTitlesOfAuthor();
                            break;
                        case 8:
                            testListAuthorsByPrefix();
                            break;
                        case 9:
                            testListByQuery();
                            break;
                        case 10:
                            testListByExpression();
                            break;
                        case 11:
                            testCreateExpression();
                            break;
                        case 12:
                            testDeleteExpression();
                            break;
                        case 13:
                            testModifyExpression();
                            break;
                        case 14:
                            cont = "N";
                            break;

                        default:
                            System.out.println("No has introduït un valor vàlid.");
                            break;
                    }
                } catch(Exception e) {
                    String missatge = e.getMessage();
                    System.out.println(missatge);
                }
                if ("S".equals(cont)) {
                    System.out.println();
                    System.out.print("Vols continuar (S/N)?: ");
                    cont = scanner.next();
                    System.out.println();
                }
            } while ("S".equals(cont));
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
        System.out.println("6 - Llistar els documents més semblants a un document");
        System.out.println("7 - Llistar els títols d'un autor");
        System.out.println("8 - Llistar els autors per prefix");
        System.out.println("9 - Llistar els documents a partir d'una query");
        System.out.println("10 - Llistar els documents a partir d'una expressió booleana");
        System.out.println("11 - Crear una expressió booleana");
        System.out.println("12 - Esborrar una expressió booleana");
        System.out.println("13 - Modificar una expressió booleana");
        System.out.println("14 - Sortir :)");
        System.out.print("Quina operació vols realitzar?: ");
    }

    public static void testCreateEmptyDocument() throws ExceptionDocumentExists {
        System.out.println("Crear un document buit");
        System.out.print("Introdueix un títol: ");
        String title = scanner.next();
        System.out.print("Introdueix un autor: ");
        String author = scanner.next();
        cd.createEmptyDocument(title, author);
        System.out.println("Document buit creat");
    }

    public static void testDeleteDocument() throws ExceptionNoDocument {
        System.out.println("Esborrar un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.next();
        System.out.print("Introdueix un autor: ");
        String author = scanner.next();
        cd.deleteDocument(title, author);
        System.out.println("Document esborrat");
    }

    public static void testExistsDocument() {
        System.out.println("Saber si existeix un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.next();
        System.out.print("Introdueix un autor: ");
        String author = scanner.next();
        if (cd.existsDocument(title, author)) System.out.println("Sí que existeix el document");
        else System.out.println("No existeix el document");
    }

    public static void testGetContentDocument() throws ExceptionNoDocument {
        System.out.println("Consultar el contingut d'un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.next();
        System.out.print("Introdueix un autor: ");
        String author = scanner.next();
        String content = cd.getContentDocument(title, author);
        System.out.println(content);
    }

    public static void testUpdateContentDocument() throws ExceptionNoDocument {
        System.out.println("Modificar el contingut d'un document");
        System.out.print("Introdueix un títol: ");
        String title = scanner.next();
        System.out.print("Introdueix un autor: ");
        String author = scanner.next();
        System.out.print("Introdueix el nou contingut: ");
        String content = scanner.next();
        cd.updateContentDocument(title, author, content);
        System.out.println("Contingut modificat");
    }

    public static void testListSimilars() {

    }

    public static void testListTitlesOfAuthor() {

    }

    public static void testListAuthorsByPrefix() {

    }

    public static void testListByQuery() {

    }

    public static void testListByExpression() {

    }

    public static void testCreateExpression() {

    }

    public static void testDeleteExpression() {

    }

    public static void testModifyExpression() {

    }
}
