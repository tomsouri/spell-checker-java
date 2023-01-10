package cz.cuni.mff.souradat.spellcheck.spellchecker;

import java.io.*;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import cz.cuni.mff.souradat.spellcheck.lexicon.ILexicon;
import cz.cuni.mff.souradat.spellcheck.lexicon.Levenshtein;
import cz.cuni.mff.souradat.spellcheck.lexicon.TrieLexicon;
import cz.cuni.mff.souradat.spellcheck.lexicon.WordAlternator;

/**
 * The Spelling checker, providing the API for external usage.
 */
public class SpellChecker {
    ILexicon lexicon;
    public SpellChecker(){
        System.out.println("Loading the lexicon. Based on its size it could take several minutes. Please, wait.");
        lexicon = new TrieLexicon();
    }

    /**
     * Add the given form to the set of known forms.
     * @param s: the word form to be added.
     * @return true if the form was not known before, false otherwise
     */
    public boolean addForm(String s){
        return lexicon.addForm(s);
    }

    /**
     * Check whether a given form is known.
     * @param s: the word form to be checked
     * @return true if the word form is known, false otherwise
     */
    public boolean contains(String s){
        return lexicon.contains(s);
    }

    /**
     * Get all words with Levenshtein distance at most 2 from the given word,
     * using the default Czech alphabet.
     * @param form: the word to be alternated
     * @return finite stream of unique alternations with distance at most 2.
     */  
    public Iterable<String> alter(String form){
        return WordAlternator.stream2iterable(
            WordAlternator.alternated(form, 2).
            filter(w -> lexicon.contains(w))
            );
    }

    /**
     * Check the content of the given reader for unknown word forms
     * and report all the unknown forms to the given writer:
     * the unknown form together with row number where it occured.
     * @param in: the reader whose content should be checked
     * @param out: the writer where the unknown forms are reported.
     * @return true if no IOException occured 
     * (and thus the checking was successful), false otherwise.
     */
    public boolean check(BufferedReader in, BufferedWriter out){
        try{
            Iterable<WordWithRowNum> allWords =  new Stream2WordsRowsNums(in);
            Iterable<WordWithRowNum> incorrectWords = filterIterable(allWords, word -> !lexicon.contains(word.word));
            out.write("row" + ":" + "\t" + "unknown" + "\n");
            for (WordWithRowNum word: incorrectWords){
                String incorrect = word.word;
                out.write(word.rowNum + ":" + "\t" + incorrect + "\n");
            }
            return true;
        }
        catch(IOException e){
            return false;
        }
    }



    public boolean correct(BufferedReader in, BufferedWriter out){
        return correct(in,out,1);
    }
   
    /**
    * Check the content of the given reader for unknown word forms
    * and report all the unknown forms to the given writer:
    * the unknown form together with row where it occured, 
    * and with suggested corrections.
    * The suggested corrections are known word forms that have 
    * Levenshtein distance from the unknown form at most `upToDistance`.
    * @param in: the reader whose content should be corrected.
    * @param out: the writer where the unknown forms and suggestions are reported.
    * @param upToDistance: the maximal Levehnstein distance of the suggestions
    * from the unknown form
    * @return true if no IOException occured 
    * (and thus the correcting was successful), false otherwise.
    */
    public boolean correct(BufferedReader in, BufferedWriter out, int upToDistance){
        try{
            Iterable<WordWithRowNum> allWords =  new Stream2WordsRowsNums(in);

            Iterable<WordWithRowNum> incorrectWords = filterIterable(allWords, word -> !lexicon.contains(word.word));
            
            out.write("row" + "\t" + "unknown" + "\t" + "->" + "\t" + "alternations (distance)" + "\n");

            for (WordWithRowNum word: incorrectWords){
                String incorrect = word.word;
                Stream<String> alternatedCorrectFormsStream = 
                    WordAlternator.alternated(incorrect, upToDistance).
                    filter(form -> lexicon.contains(form));

                Iterable<String> alternatedCorrectForms = WordAlternator.stream2iterable(alternatedCorrectFormsStream);
                
                // Iterable<String> alternatedForms = WordAlternator.iterateAlternatedWords(incorrect);
                // Iterable<String> alternatedCorrectForms = filterIterable(alternatedForms, form -> lexicon.contains(form));
                
                out.write(word.rowNum + ":" + "\t" + incorrect + "\t" + "->" + "\t");

                int counter = 0;
                for (String alternated: alternatedCorrectForms){
                    out.write(alternated + " (" + Levenshtein.distance(alternated, incorrect) + ")");
                    if (counter++ > 5) { break; }
                    else{
                        out.write(", ");
                    }
                }

                out.write("\n");
            }
            return true;
        }
        catch(IOException e){
            return false;
        }
    }

    /**
     * Alternative to the filter method on Stream, to be used on Iterable.
     * Works the same.*
     * * Could be done easily by converting to stream, applying the filter 
     * method on the stream and then converting back to iterable.
     * @param <T>: type of elements in the iterable
     * @param iterable: iterable to be filtered
     * @param predicate: predicate used for filtering
     * @return the filtered iterable
     * @see java.util.stream.Stream
     */
    private <T> Iterable<T> filterIterable(Iterable<T> iterable, Predicate<T> predicate){
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>(){

                    private T getNextElem() {
                        T nextElem;
                        do {
                            if (!allElems.hasNext()){
                                nextElem = null;
                                break;
                            }
                            nextElem = allElems.next();
                        } while (!predicate.test(nextElem));
                        return nextElem;
                    }
        
                    private Iterator<T> allElems = iterable.iterator();
                    private T bufferedElem = getNextElem();
                    
        
                    @Override
                    public boolean hasNext() {
                        return bufferedElem != null;
                    }
        
                    @Override
                    public T next() {
                        T toReturn = bufferedElem;
                        bufferedElem = getNextElem();
                        return toReturn;
                    }};
            }
        }; 
    }
}