package main.excepcions;

/**
 * @class ExceptionInvalidStrategy
 * @brief Excepció que es llença quan el paràmetre d'estratègia no és vàlid
 * @author pau.duran.manzano
 */
public class ExceptionInvalidStrategy extends Exception{

    /**
     * @brief Constructora per defecte
     * @details Donada l'estratègia introduïda, es genera el missatge d'error
     * @return ExceptionInvalidStrategy
     */
    public ExceptionInvalidStrategy(String strategy){
        super("L'estratègia " + strategy + " no és vàlida. Es permet 'tf-idf' o 'tf-boolean'");
    }
}