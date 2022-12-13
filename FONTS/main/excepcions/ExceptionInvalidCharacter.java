package main.excepcions;

/**
 * @class ExceptionInvalidCharacter
 * @brief Excepció que es llença quan no es troba una expressió al sistema
 * @author ariadna.cortes.danes
 */
public class ExceptionInvalidCharacter extends Throwable {
    /**
     * @brief Constructora per defecte
     * @details Donat un caracter no valid en un XML, es genera el missatge d'error de l'excepció
     * @return ExceptionInvalidCharacter
     */
    public ExceptionInvalidCharacter(String format){
        super("El character o etiqueta " + format + " no és vàlid en un XML");
    }
}
