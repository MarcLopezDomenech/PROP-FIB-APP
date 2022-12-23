package main.excepcions;

/**
 * @class ExceptionInvalidLanguage
 * @brief Excepció que es llença quan el paràmetre d'idioma d'un document no és vàlid
 * @author pau.duran.manzano
 */
public class ExceptionInvalidLanguage extends Exception {

    /**
     * @brief Constructora per defecte amb el llenguatge invàlid
     * @details Donat el llenguatge que no és vàlid, es genera el missatge d'error
     * @return ExceptionInvalidLanguage
     */
    public ExceptionInvalidLanguage(String language){
        super("L'idioma d'un document pot ser català (ca), anglès (en) o castellà (es), " + language + " no és vàlid");
    }
}