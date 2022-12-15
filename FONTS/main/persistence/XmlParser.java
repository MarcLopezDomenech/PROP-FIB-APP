package main.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import main.excepcions.ExceptionInvalidCharacter;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionMissingTitleOrAuthor;

/**
 * @class TxtParser
 * @brief Classe responsable de la lectura i escriptura de fitxers en format txt
 * @author ariadna.cortes.danes
 */
public class XmlParser extends Parser {
    
    /**
     * @brief Constructora de la classe
     */
    public XmlParser() {

    }


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
        content = removeScapeChars(content);

        return title + "@title@" + author + "@author@" + content + "@content@";
    }

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

        String str = "<title>" + title + "</title>\n<author>" + author + "</author>\n<content>" + content + "</content>";
        writeToFile(str, path);
    }

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
