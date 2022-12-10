package main.domain.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @class Trie
 * @brief Implementació de l'arbre Trie
 * @author marc.valls.camps
 */
public class Trie {
    /**
     * @class Node
     * @brief Nodes de l'arbre, cadascun representa un determinat prefix
     * @author marc.valls.camps
     */
    private static class Node {
        /**
         * \brief Nodes descendents, representant cadascun una lletra extenent el prefix del Node pare
         */
        public HashMap<Character,Node> descendants;

        /**
         * \brief Enter que guarda quantes vegades el prefix representat per aquest Node s'ha afegit a l'estructura
         */
        public int timesWord;

        /**
         * @brief Constructora per defecte
         * @return Una instància de Node inicialitzada amb els valors per defecte
         */
        public Node() {
            descendants = new HashMap<>();
            timesWord = 0;
        }
    }

    /**
     * \brief El Node arrel de l'estructura
     */
    private final Node root;

    /**
     * @brief Constructora per defecte
     * @return Una instància de Trie amb només el Node arrel, representant el prefix buit
     */
    public Trie(){
        root = new Node();
    }

    /**
     * @brief Recorre l'arbre fins al Node que representa el prefix word
     * @param word Paraula que s'ha de cercar en l'arbre
     * @param create Booleà que indica si s'ha de crear els Nodes que faltin
     * @return El Node a buscar si existia. Si no existia i el paràmetre create és cert aleshores s'ha creat i es retorna el nou Node,
     * si era fals aleshores es retorna null.
     */
    private Node traverseToNode(String word, boolean create) {
        Node n = root;
        for (int i = 0; i < word.length(); ++i) {
            if (!n.descendants.containsKey(word.charAt(i))) {
                if (create) n.descendants.put(word.charAt(i), new Node());
                else return null;
            }
            n = n.descendants.get(word.charAt(i));
        }
        return n;
    }

    /**
     * @brief Inserció d'una paraula a l'arbre
     * @param word Paraula a incrementar-ne el nombre d'aparicions en l'estructura en una unitat
     */
    public void insertOnce(String word) {
        Node n = traverseToNode(word, true);
        ++n.timesWord;
    }

    /**
     * @brief Esborrat d'una instància d'una paraula a l'arbre
     * @param word Paraula a decrementar-ne el nombre d'aparicions en l'estructura
     * @return Cert ssi existia la paraula en l'arbre i s'ha pogut decrementar el nombre de vegades
     * que és continguda en l'arbre
     */
    public boolean removeOnce(String word) {
        Node n = traverseToNode(word, false);
        if (n == null || n.timesWord == 0) return false;

        --n.timesWord;
        return true;
    }

    /**
     * @brief Cerca d'una paraula en l'arbre
     * @param word Paraula a buscar
     * @return Cert ssi existia la paraula en l'arbre
     */
    public boolean contains(String word) {
        Node n = traverseToNode(word, false);
        return n != null && n.timesWord > 0;
    }

    /**
     * @brief Cerca de totes les paraules donat un prefix
     * @param prefix Prefix de les paraules a trobar
     * @return El conjunt de les paraules de l'arbre que comencen amb el prefix especificat
     */
    public ArrayList<String> wordsGivenPrefix(String prefix) {
        Node n = traverseToNode(prefix, false);
        return recursiveCollect(prefix, n);
    }

    /**
     * @brief Llistat de les paraules que pengen d'un Node
     * @param prefix Prefix que s'afegirà a totes les paraules que es retornin
     * @param n Node arrel de l'arbre a explorar
     * @return Conjunt de les paraules que pengen del Node n, començant pel prefix donat
     */
    private static ArrayList<String> recursiveCollect(String prefix, Node n) {
        ArrayList<String> result = new ArrayList<>();
        if (n == null) return result;

        if (n.timesWord > 0) result.add(prefix);
        for (java.util.Map.Entry<Character, Node> desc : n.descendants.entrySet()) {
            result.addAll(recursiveCollect(prefix + desc.getKey(), desc.getValue()));
        }

        return result;
    }
}