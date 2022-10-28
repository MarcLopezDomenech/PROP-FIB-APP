package main.excepcions;

/**
 * @class ExceptionExpressionExists
 * @brief Excepció que es llença quan l'expressió ja existeix
 * @author pau.duran.manzano
 */
public class ExceptionExpressionExists extends Exception{

    /**
     * @brief Constructora per defecte
     * @details A partir de l'identificador de l'expressió que ja existeix, es genera el missatge per defecte
     * @return ExceptionExpressionExists
     */
    public ExceptionExpressionExists(String id_expression){
        super("Ja existeix una expressió booleana amb identificador " + id_expression);
    }
}