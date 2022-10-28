package main.excepcions;

/**
 * @class ExceptionDocumentExists
 * @brief Excepció que es llença quan el document ja existeix
 * @author pau.duran.manzano
 */
public class ExceptionDocumentExists extends Exception{

    /**
     * @brief Constructora per defecte
     * @details A partir del title i author del document que ja existeix, es genera el missatge per defecte
     * @return ExceptionDocumentExists
     */
    public ExceptionDocumentExists(String title, String author){
        super("Ja existeix un document amb títol '"+ title + "' i autor '" + author);
    }
}