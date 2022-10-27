package main.domain.util;

/**
 * @class Pair
 * @brief Implementació senzilla de la classe Pair per poder-la usar en el nostre projecte, només amb les operacions que ens interessen
 * @author pau.duran.manzano
 */
public class Pair<F, S> {
    private F first;        // Primer camp del Pair
    private S second;       // Segon camp del Pair

    /**
     * @brief Constructora per defecte
     */
    public Pair() {
    }

    /**
     * @brief Constructora amb inicialització
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @brief Funció per modificar el primer valor del Pair
     * @param first Valor a assignar en el primer valor del Pair
     * @post El Pair té com a primer valor el paràmetre donat
     */
    public void setFirst(F first) {this.first = first;}

    /**
     * @brief Operació per obtenir el primer camp del Pair
     * @return S'obté l'element que correspon al primer camp de l'objecte
     * @post El Pair no queda alterat
     */
    public F getFirst() {return this.first;}

    /**
     * @brief Funció per modificar el segon valor del Pair
     * @param second Valor a assignar en el primer valor del Pair
     * @post El Pair té com a segon valor el paràmetre donat
     */
    public void setSecond(S second) {this.second = second;}

    /**
     * @brief Operació per obtenir el segon camp del Pair
     * @return S'obté l'element que correspon al segon camp de l'objecte
     * @post El Pair no queda alterat
     */
    public S getSecond() {return this.second;}

}
