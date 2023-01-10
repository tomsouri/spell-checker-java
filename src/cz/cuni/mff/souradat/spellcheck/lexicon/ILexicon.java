package cz.cuni.mff.souradat.spellcheck.lexicon;

/**Represents a set of known word forms and provides useful methods.
 */
public interface ILexicon {
    /**
     * Checks, whether a given word form is present in the lexicon.
     * @param form: the word form to be checked.
     * @return true if the word form is present, false otherwise
     */
    public boolean contains(String form);

    /**
     * Adds the given word form to the lexicon.
     * @param form: the word form to be added.
     * @return true if the word form was 
     * not contained in the lexicon before the addition,
     * false otherwise
     */
    public boolean addForm(String form);

    /** Lowercase the first character of the given word.
     * @param word: the word whose first character should be lowercased.
     * @return the word with lowercased first character,
     * or unchanged word if the first character was not upper cased.
     */
    public static String removeCapitalization(String word){
        if (word.length() == 0){
            return word;
        }
        char first = word.charAt(0);

        if (Character.isUpperCase(first)){
            return Character.toString((Character.toLowerCase(first))) + word.substring(1, word.length());
        }
        else{
            return word;
        }
    }

    /**
     * Lowercase the whole word if it is uppercased.
     * @param word: word to be lowercased.
     * @return lowercased word if it was uppercased,
     * otherwise unchanged word
     */
    public static String toLowerCaseIfUpperCase(String word){
        if (isUpperCase(word)){
            return word.toLowerCase();
        }
        else {
            return word;
        }
    }

    /**
     * Check, whether the given word is completely uppercased 
     * (contains only uppercase characters).
     * @param word: word to be checked.
     * @return true if the word is completely uppercased, false otherwise.
     */
    public static boolean isUpperCase(String word){
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);
            if (Character.isLowerCase(c)){
                return false;
            }
        }
        return true;
    }

    /** Convert the word to Lowercased Variant
     * With Capitalized First Character, but only
     * if it was completely uppercased.
     * @param word to be lowercased and capitalized.
     * @return lowercased and capitalized word if the word was uppercased,
     * otherwise unchanged word
     */
    public static String toCapitalizedLowerCaseIfUpperCase(String word){
        if (word.isEmpty() || word.length()== 1 || !isUpperCase(word)){return word;}
        return Character.toString(word.charAt(0)) + word.substring(1, word.length()).toLowerCase();
        
    }
}