package main.excepcions;

/**
 * @class ExceptionInvalidLanguage
 * @brief Excepció que es llença quan el paràmetre d'idioma d'un document no és vàlid
 * @author pau.duran.manzano
 */
public class ExceptionInvalidLanguage extends Exception {

    /**
     * @brief Constructora per defecte amb la k errònia
     * @details Donada la k introduïda, es genera el missatge d'error
     * @return ExceptionInvalidStrategy
     */
    public ExceptionInvalidLanguage(String language){
        super("L'idioma d'un document pot ser català (ca), anglès (en) o castellà (es), " + language + " no és vàlid");
    }
}