package main.domain.util;

import java.util.ArrayList;

/**
 * @class Trie
 * @brief Implementació de l'arbre Trie
 * @author marc.valls.camps
 */
public class Trie {
    private class Node {
        /**
         * \brief Primer camp de la parella
         */
        public Node[] descendants;

        public boolean isEndWord;

        public Node() {
            descendants = new Node[26]; // corregir
            isEndWord = false;
        }
    }

    private Node t;

    public Trie(){
        t = new Node();
    }

    public boolean insert(String s) {
        Node n = t;
        for (int i = 0; i < s.length(); ++i) {
            if (n.descendants[s.charAt(i) - 'a'] == null) n.descendants[s.charAt(i) - 'a'] = new Node();
            n = n.descendants[s.charAt(i) - 'a'];
        }
        if (n.isEndWord) return false;
        n.isEndWord = true;
        return true;
    }

    public boolean remove(String s) {
        Node n = t;
        for (int i = 0; i < s.length() - 1; ++i) {
            if (n.descendants[s.charAt(i) - 'a'] == null) return false;
            n = n.descendants[s.charAt(i) - 'a'];
        }
        if (n.descendants[s.charAt(s.length() - 1) - 'a'] == null) return false;
        n.descendants[s.charAt(s.length() - 1) - 'a'] = null;
        return true;
    }

    public boolean contains(String s) {
        Node n = t;
        for (int i = 0; i < s.length(); ++i) {
            if (n.descendants[s.charAt(i) - 'a'] == null) return false;
            n = n.descendants[s.charAt(i) - 'a'];
        }
        return n.isEndWord;
    }

    public ArrayList<String> wordsGivenPrefix(String prefix) {
        Node n = t;
        for (int i = 0; i < prefix.length(); ++i) {
            if (n.descendants[prefix.charAt(i) - 'a'] == null) return new ArrayList<String>(0);
            n = n.descendants[prefix.charAt(i) - 'a'];
        }
        return recursiveCollect(prefix, n);
    }

    private static ArrayList<String> recursiveCollect(String s, Node n) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < n.descendants.length; ++i)
            result.addAll(recursiveCollect(s + (char)('a'+ i), n.descendants[i]));

        if (n.isEndWord) result.add(s);
        return result;
    }
}