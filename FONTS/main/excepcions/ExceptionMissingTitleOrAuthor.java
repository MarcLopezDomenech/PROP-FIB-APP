package main.excepcions;

/**
 * @class ExceptionMissingTitleOrAuthor
 * @brief Excepció que es llença quan un document conté autor i títol null, o etiquetes incorrectes en xml
 * @author ariadna.cortes.danes
 */
public class ExceptionMissingTitleOrAuthor extends Exception {
    /**
     * @brief Constructora per defecte
     * @details Donat un caràcter no valid en un XML, es genera el missatge d'error de l'excepció
     * @return ExceptionInvalidCharacter
     */
    public ExceptionMissingTitleOrAuthor(String titleOrAuthor){
        super("El document requereix " + titleOrAuthor + ". Comprova el format de l'entrada");
    }
}
