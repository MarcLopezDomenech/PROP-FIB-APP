package main.excepcions;

/**
 * @class ExceptionNoExpression
 * @brief Excepció que es llença quan no es troba una expressió al sistema
 * @author pau.duran.manzano
 */
public class ExceptionNoExpression extends Exception{

    /**
     * @brief Constructora per defecte
     * @details A partir de l'identificar de l'expressió booleana que no existeix, es genera el missatge per defecte
     * @return ExceptionNoExpression
     */
    public ExceptionNoExpression(String id_expressio){
        super("No existeix cap expressió booleana identificada per " + id_expressio);
    }
}