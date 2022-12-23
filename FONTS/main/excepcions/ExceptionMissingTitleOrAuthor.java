package main.excepcions;

/**
 * @class ExceptionMissingTitleOrAuthor
 * @brief Excepció que es llença quan s'intenta donar d'alta o carregar un document sense títol i/o autor, o bé són invàlids
 * @author ariadna.cortes.danes
 */
public class ExceptionMissingTitleOrAuthor extends Exception {
    /**
     * @brief Constructora per defecte
     * @details Donat un String indicant si falta títol, autor o ambdós, genera un missatge d'error
     * @return ExceptionMissingTitleOrAuthor
     */
    public ExceptionMissingTitleOrAuthor(String titleOrAuthor){
        super("El document requereix " + titleOrAuthor + ". Comprova el format de l'entrada");
    }
}
