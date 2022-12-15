package main.excepcions;

/**
 * @class ExceptionInvalidCharacter
 * @brief Excepció que es llença quan no es troba una expressió al sistema
 * @author ariadna.cortes.danes
 */
public class ExceptionMissingTitleOrAuthor extends Exception {
    /**
     * @brief Constructora per defecte
     * @details Donat un caracter no valid en un XML, es genera el missatge d'error de l'excepció
     * @return ExceptionInvalidCharacter
     */
    public ExceptionMissingTitleOrAuthor(String titleOrAuthor){
        super("El document requereix " + titleOrAuthor + ". Comprova el format de l'entrada");
    }
}
