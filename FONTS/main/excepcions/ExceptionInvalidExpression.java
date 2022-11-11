package main.excepcions;

/**
 * @class ExceptionInvalidExpression
 * @brief Excepció que es llença quan l'expressió no està ben formada
 * @author pau.duran.manzano i marc.valls.camps
 */
public class ExceptionInvalidExpression extends Exception{

    /**
     * @brief Constructora per defecte
     * @details Donada una expressió boolana que no té un format correcte, excepció informa a l'usuari de l'error.
     * @return ExceptionInvalidExpression
     */
    public ExceptionInvalidExpression(String id_expressio){
        super("L'expressió " + id_expressio + " no té un format correcte");
    }
}