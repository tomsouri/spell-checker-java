package cz.cuni.mff.souradat.spellcheck.trie;

import java.io.Serializable;
import java.util.stream.Stream;

/**Character-based Trie datastructure.
 * Works like a simple (and memory saving) set of strings. 
 * Based on an example from https://www.baeldung.com/trie-java
 */
public class Trie implements Serializable {
    private TrieNode root = new TrieNode();

    /**Creates Trie from a Stream of Strings.
     * @param stream: Stream of Strings representing
     * the words that are added to the trie.
     * @return the built trie.
     */
    public static Trie fromStream(Stream<String> stream){
        Trie trie = new Trie();
        Iterable<String> forms = () -> stream.iterator();
        for (String form: forms){
            trie.add(form);
        }
        return trie;
    }

    /**Add a given word to the trie.
     * @param word: the word to be added.
     */
    public void add(String word) {
        TrieNode current = root;
    
        for (char l: word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    /**Checks, whether a given word is present in the trie.
     * @param word: the word to be checked
     * @return true/false
     */
    public boolean contains(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }
}