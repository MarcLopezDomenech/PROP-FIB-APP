package main.excepcions;

/**
 * @class ExceptionNoDocument
 * @brief Excepció que es llença quan no es troba un document al sistema
 * @author pau.duran.manzano
 */
public class ExceptionNoDocument extends Exception{

    /**
     * @brief Constructora per defecte
     * @details A partir del title i author del document que no existeix, es genera el missatge per defecte
     * @return ExceptionNoDocument
     */
    public ExceptionNoDocument(String title, String author){
        super("El document amb títol '" + title + "' i autor '" + author + "' no existeix");
    }
}