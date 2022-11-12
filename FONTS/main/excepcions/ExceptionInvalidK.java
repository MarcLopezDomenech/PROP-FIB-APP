package main.excepcions;

/**
 * @class ExceptionInvalidK
 * @brief Excepció que es llença quan el paràmetre k d'alguna funció és menor que 0 o invàlid
 * @author pau.duran.manzano
 */
public class ExceptionInvalidK extends Exception {

    /**
     * @brief Constructora per defecte
     * @details Funció llençada quan k no es pot processar (segurament no sigui un enter)
     * @return ExceptionInvalidStrategy
     */
    public ExceptionInvalidK(){
        super("El valor de k ha de ser un enter major o igual a 0");
    }

    /**
     * @brief Constructora per defecte amb la k errònia
     * @details Donada la k introduïda, es genera el missatge d'error
     * @return ExceptionInvalidStrategy
     */
    public ExceptionInvalidK(int k){
        super(k + " no és vàlid, el valor de k ha de ser un enter major o igual a 0");
    }
}