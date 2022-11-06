package main.excepcions;

/**
 * @class ExceptionInvalidFormat
 * @brief Excepció que es llença quan no es troba una expressió al sistema
 * @author ariadna.cortes.danes
 */
public class ExceptionInvalidFormat extends Exception{

    /**
     * @brief Constructora per defecte
     * @details Donat un format no valid, es genera el missatge d'error de l'excepcio
     * @return ExceptionInvalidFormat
     */
    public ExceptionInvalidFormat(String format){
        super("El format " + format + " no és vàlid");
    }
}