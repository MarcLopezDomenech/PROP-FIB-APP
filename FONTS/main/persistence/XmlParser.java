package main.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import main.excepcions.ExceptionInvalidCharacter;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionMissingTitleOrAuthor;

/**
 * @class XmlParser
 * @brief Classe responsable de la lectura i escriptura de fitxers en format xml
 * @author ariadna.cortes.danes
 */
public class XmlParser extends Parser {
    
    /**
     * @brief Constructora de la classe
     */
    public XmlParser() {

    }

    /**
     * @brief Llegeix el string que troba en el fitxer corresponent al path que rep en format xml
     * @param path El path d'on es llegirà
     * @return String en format propietari representant el document xml llegit
     * @throws FileNotFoundException Excepció llençada si no es troba el fitxer corresponent al path donat
     * @throws ExceptionInvalidCharacter Si es troba algun Scape Character d'XML mal gestionat
     * @throws ExceptionMissingTitleOrAuthor Si falta alguna etiqueta o el autor o titol son buits
     */
    public String read(String path) throws FileNotFoundException, ExceptionInvalidCharacter, ExceptionMissingTitleOrAuthor {
        String input = readFromFile(path);
        String[] information = input.split("<title>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta <title>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("<title>");
        information = information[1].split("</title>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta </title>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("</title>");
        String title = information[0];
        input = information[1];

        //obtenir el autor
        information = input.split("<author>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta <author>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("<author>");
        information = information[1].split("</author>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta </author>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("</author>");
        String author = information[0];
        input = information[1];

        //Obtenir el contingut
        information = input.split("<content>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta <content>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("<content>");
        information = information[1].split("</content>");
        if (information.length == 1) throw new ExceptionMissingTitleOrAuthor("l'etiqueta </content>");
        if (information.length > 2) throw new ExceptionInvalidCharacter("</content>");
        String content = information[0];

        //En xml, hi ha certs caràcters especials (scape characters), que no corresponen al seu valor ascii
        title = removeScapeChars(title);
        author = removeScapeChars(author);
        content = removeScapeChars(content);

        return title + "@title@" + author + "@author@" + content + "@content@";
    }

    /**
     * @brief Funció per a escriure en format xml un cert contingut en un fitxer al path corresponent
     * @param document string a escriure
     * @param path El path on s'escriurà
     * @throws IOException Excepció llençada en error d'escriptura al fitxer corresponent al path
     */
    public void write(String document, String path) throws IOException {

        //Obtenir el titol
        String[] information = document.split("@title@");
        String title = information[0];
        document = information[1];
    
        //obtenir el autor
        information = document.split("@author@");
        String author = information[0];
        document = information[1];

        //Obtenir el contingut 
        information = document.split("@content@");
        String content = information[0];

        content = addScapeChars(content);
        title = addScapeChars(title);
        author = addScapeChars(author);

        String str = "<title>" + title + "</title>\n<author>" + author + "</author>\n<content>" + content + "</content>";
        writeToFile(str, path);
    }

    /**
     * @brief Canvia els scape characters per UTF-8 caràcters normals
     * @param str El text a processar els scape characters
     * @return El mateix text amb scape characters reemplaçats per caràcters normals
     * @throws ExceptionInvalidCharacter Si es troba algun Scape Character d'XML mal gestionat
     */
    private String removeScapeChars(String str) throws ExceptionInvalidCharacter {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\'') throw new ExceptionInvalidCharacter("\'");
            if (str.charAt(i) == '\"') throw new ExceptionInvalidCharacter("\"");
            if (str.charAt(i) == '<') throw new ExceptionInvalidCharacter("<");
            if (str.charAt(i) == '>') throw new ExceptionInvalidCharacter(">");
            if (str.charAt(i) == '&')  {
                if (str.charAt(i+1) == 'q' && str.charAt(i+2) == 'u' && str.charAt(i+3) == 'o' && str.charAt(i+4) == 't') {
                    str = (str.substring(0, i)).concat("\"" + str.substring(i+5));
                }
                else if (str.charAt(i+1) == 'a' && str.charAt(i+2) == 'p' && str.charAt(i+3) == 'o' && str.charAt(i+4) == 's') {
                    str = str.substring(0, i).concat("\'" + str.substring(i+5));
                }
                else if (str.charAt(i+1) == 'l' && str.charAt(i+2) == 't') {
                    str = (str.substring(0, i)).concat("<" + str.substring(i+3));
                }
                else if (str.charAt(i+1) == 'g' && str.charAt(i+2) == 't') {
                    str = str.substring(0, i).concat(">" + str.substring(i+3));
                }
                else if (str.charAt(i+1) == 'a' && str.charAt(i+2) == 'm' && str.charAt(i+3) == 'p') {
                    str = str.substring(0, i).concat("&" + str.substring(i+4));
                } else throw new ExceptionInvalidCharacter("&");
            }
        }
        return str;
    }

    /**
     * @brief Canvia  els UTF-8 caràcters normals per scape characters propis d'XML
     * @param str  El text a processar per afegir els scape characters
     * @return El mateix text amb els caràcters necessaris reemplaçats per scape characters de xml
     */
    private String addScapeChars(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\'')  {
                str = (str.substring(0, i)).concat("&apos" + str.substring(i+1));
            }
            else if (str.charAt(i) == '\"')  {
                str = (str.substring(0, i)).concat("&quot" + str.substring(i+1));
            }
            else if (str.charAt(i) == '&')  {
                str = (str.substring(0, i)).concat("&amp" + str.substring(i+1));
            }
            else if (str.charAt(i) == '<')  {
                str = (str.substring(0, i)).concat("&lt" + str.substring(i+1));
            }
            else if (str.charAt(i) == '>')  {
                str = (str.substring(0, i)).concat("&gt" + str.substring(i+1));
            }
        }
        return str;
    }
}
