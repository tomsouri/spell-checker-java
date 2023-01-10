package cz.cuni.mff.souradat.spellcheck.trie;

import java.io.Serializable;
import java.util.HashMap;

/** Building block of the character-based trie.
 * @see cz.cuni.mff.souradat.spellcheck.trie.Trie
 */
public class TrieNode implements Serializable {

    /** Dictionary of children.*/
    private HashMap<Character, TrieNode> children = new HashMap<>();
    private boolean isWord;

    /**Sets the property of being end of a word.
     * @param bool - is or is not the end of a word.
     */
    public void setEndOfWord(boolean bool){
        isWord = bool;
    }

    /** Get the property of being end of a word.
     * @return true/false.
     */
    public boolean isEndOfWord(){
        return isWord;
    }

    /** Get the dictionary of children.
     * @return the dictionary of children of the current node.
     */
    public HashMap<Character, TrieNode> getChildren(){
        return children;
    }

    
}