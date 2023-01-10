package cz.cuni.mff.souradat.spellcheck.lexicon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.stream.IntStream;

import cz.cuni.mff.souradat.spellcheck.lexicon.Levenshtein;

/**
 * Class for creating all possible alternations of a word.
 * The allowed operations are deletion/insertion of a character
 * and replacement of one character by another one.
 */
public class WordAlternator {

    /**
     * The default Czech alphabet to be used for alternations.
     */
    public static Set<Character> defaultAlphabet = new HashSet<Character>();
    static {
        for (char c = 'a'; c<= 'z'; c++){
            defaultAlphabet.add(c);
            defaultAlphabet.add(Character.toUpperCase(c));
        }
        for (char c: new char[]{'á', 'é', 'í', 'ý', 'ó', 'ú', 'ů', 'ž', 'š', 'č', 'ř', 'ď', 'ť', 'ň', 'ě'}){
            defaultAlphabet.add(c);
            defaultAlphabet.add(Character.toUpperCase(c));
        }
    }

    /** Generates all words that can be created from the given word,
     * ordered by increasing Levehnstein distance of the generated
     * word and the given word, using the default alphabet
     * @param word: word to be alternated.
     * @return infinite stream of unique alternations
     */
    public static Stream<String> alternated(String word){
        return alternated(word, defaultAlphabet);
    }

    /** Generates all words that can be created from the given word,
     * whose distance from the given word is at most `upToDistance`,
     * ordered by increasing Levehnstein distance of the generated
     * word and the given word, using the default alphabet  
     * @param word: word to be alternated.
     * @param upToDistance: maximal Levenshtein distance of the alteration
     * from the initial word
     * @return finite stream of unique alternations
     */
    public static Stream<String> alternated(String word, int upToDistance){
        return alternated(word, upToDistance, defaultAlphabet);
    }

    /** Generates all words that can be created from the given word,
     * whose distance from the given word is at most `upToDistance`,
     * ordered by increasing Levehnstein distance of the generated
     * word and the given word, using the given alphabet  
     * @param word: word to be alternated.
     * @param upToDistance maximal Levenshtein distance of the alteration
     * from the initial word
     * @param alphabet: the given alphabet
     * @return finite stream of unique alternations
     */
    public static Stream<String> alternated(String word, int upToDistance, Set<Character> alphabet){
        List<Character> alphabetList = new ArrayList<Character>(alphabet);
        return IntStream.range(1, upToDistance + 1).
            mapToObj(i -> wordsWithDistN(word, alphabetList, i)).
            flatMap(Function.identity());
    }

    /**
     * Generates all words with Levenshtein distance 1 from the given word,
     * using the given alphabet.
     * @param word: the word to be alternated
     * @param alphabet: the alphabet to be used for alternations.
     * @return finite stream of unique alternations with distance 1
     */
    private static Stream<String> wordsWithDistOne(String word, List<Character> alphabet){
        // 
        return iterable2stream(wordsWithDistOneIterable(word, alphabet));
    }

    /**
     * Generates all words with Levenshtein distance n from the given word,
     * using the given alphabet.
     * @param word: the word to be alternated
     * @param alphabet: the alphabet to be used for alternations.
     * @param n: the specified Levenshtein distance that the generated
     * words should have from the given word
     * @return finite stream of unique alternations with distance n
     */    
    private static Stream<String> wordsWithDistN(String word, List<Character> alphabet, int n){
        Stream<String> words = wordsWithDistOne(word, alphabet);
        for (int i=2; i<=n; i++){
            final int j = i;
            words = words.flatMap( w -> wordsWithDistOne(w, alphabet)).
                filter(w -> Levenshtein.distance(w, word) == j).
                distinct();
        }
        return words;
    }

    /** Generates all words that can be created from the given word,
     * ordered by increasing Levehnstein distance of the generated
     * word and the given word, using the given alphabet
     * @param word: word to be alternated.
     * @param alphabet: the alphabet to be used for the alternations
     * @return infinite stream of unique alternations
     */
    public static Stream<String> alternated(String word, Set<Character> alphabet){
        List<Character> alphabetList = new ArrayList<Character>(alphabet);
        return IntStream.iterate(1, i -> i + 1).
            mapToObj(i -> wordsWithDistN(word, alphabetList, i)).
            flatMap(Function.identity());
    }


    /**
     * Generates all words with Levenshtein distance 1 from the given word,
     * using the given alphabet.
     * @param word: the word to be alternated
     * @param alphabet: the alphabet to be used for alternations.
     * @return finite Iterable of unique alternations with distance 1
     */
    private static Iterable<String> wordsWithDistOneIterable(String word, List<Character> alphabet){
        
        /**
         * Represents the iterable of all words with Levenshtein distance 1 
         * from the given word, using the given alphabet.
         */
        class DistanceOneWordsIterable implements Iterable<String> {
            private String word;
            private List<Character> alphabet;

            public DistanceOneWordsIterable(String word, List<Character> alphabet){
                this.word = word;
                this.alphabet = alphabet;
            }

            @Override
            /**
             *  Generates all words that can be created from the given word,
             *  that have Levenshtein distance from the given word equal 1.
             *  These words are obtained by deleting a char, inserting a
             *  char or replacing a char. It looks a little bit tricky when
             *  it is implemented as an iterable, but it is as simple as
             *  iterating over all possible changing operations (delete,
             *  insert, replace), iterate over all possible characters in
             *  the alphabet and iterate over all possible places in the
             *  given word, where the change should happen.
             */
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int alternationMethod = 0;
                    int indexInWord = 0;
                    int indexOfChar = 0;
                    boolean hasNext = true;

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public String next() {
                        String result = null;
                        switch(alternationMethod){
                            case 0 :    
                                result = insertCharAt(word, indexInWord, alphabet.get(indexOfChar++));
                                break;
                            case 1 :
                                result = replaceCharAt(word, indexInWord, alphabet.get(indexOfChar++));
                                break;
                            case 2 :
                                result = deleteCharAt(word, indexInWord++);
                                break;
                            default :
                                throw new Error("Unknown alternation method.");
                        }
                        if (alternationMethod == 0){
                            if (indexOfChar == alphabet.size()){
                                indexOfChar = 0;
                                indexInWord++;
                            }
                            if (indexInWord > word.length()){
                                alternationMethod++;
                                indexInWord = 0;
                            }
                        }
                        if (alternationMethod == 1){
                            if (indexOfChar == alphabet.size()){
                                indexOfChar = 0;
                                indexInWord++;
                            }
                            if (indexInWord >= word.length()){
                                alternationMethod++;
                                indexInWord = 0;
                            }
                        }
                        if (alternationMethod == 2){
                            if(indexInWord >= word.length()){
                                hasNext = false;
                            }
                        }
                        return result;
                    }
                    
                    
                    /**
                     * Inserts a character at a specific position in a string
                     * @param word: the word to be changed
                     * @param position: the specific position
                     * @param c: the new character
                     * @return the changed word
                     */
                    private String insertCharAt(String word, int position, char c) {
                        return word.substring(0, position) + c + word.substring(position);
                    }

                    /** 
                     * Deletes the character at a specific position in a string
                     * @param word: the word to be changed
                     * @param position: the specific position
                     * @return the changed word
                     */
                    private String deleteCharAt(String word, int position) {
                        return word.substring(0, position) + word.substring(position + 1);
                    }

                    /** Replaces the character at a specific position 
                     * in a string with a new character
                     * @param word: the word to be changed
                     * @param position: the specific position
                     * @param c: the new character
                     * @return the changed word
                     */
                    private String replaceCharAt(String word, int position, char c) {
                        return word.substring(0, position) + c + word.substring(position + 1);
                    }
                
                };
            }
        }
        return new DistanceOneWordsIterable(word, alphabet);

    }

    /**
     * Convert an iterable to a stream.
     * @param <T> the type of the elements in the iterable.
     * @param iter: the given iterable to be converted to stream.
     * @return the stream containing the same elements as the given iterable
     */
    private static <T> Stream<T> iterable2stream(Iterable<T> iter){
        return StreamSupport.stream(iter.spliterator(), false);
    }

    /**
     * Convert a stream to an iterable.
     * @param <T> the type of the elements in the stream.
     * @param stream: the given stream to be converted to iterable.
     * @return the iterable containing the same elements as the given stream
     */
    public static <T> Iterable<T> stream2iterable(Stream<T> stream){
        return () -> stream.iterator();
    }
}
