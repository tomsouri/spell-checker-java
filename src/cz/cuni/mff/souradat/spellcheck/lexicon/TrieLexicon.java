package cz.cuni.mff.souradat.spellcheck.lexicon;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;

import cz.cuni.mff.souradat.spellcheck.trie.Trie;

/**
 * ILexicon that internally uses Trie to store the known forms.
 * @see cz.cuni.mff.souradat.spellcheck.lexicon.ILexicon
 * @see cz.cuni.mff.souradat.spellcheck.trie.Trie
 */
public class TrieLexicon implements ILexicon {
    private static final String serializedFilename = "trielexicon.ser";
    private static final String morfflexFilename = "czech-morfflex-2.0.tsv";

    private Trie lexicon;

    public TrieLexicon(){
        lexicon = loadLexicon();
    }

    /**
     * Load the trie lexicon, either from the serialized file
     * or directly build the trie from the morfflex file
     * @return the built Trie
     * @see cz.cuni.mff.souradat.spellcheck.trie.Trie
     */
    private Trie loadLexicon(){
        File f = new File(serializedFilename);
        Trie lex;
        
        if(!f.exists() || f.isDirectory()) { 
            lex = loadMorfflex();
            serializeLexicon(lex);
        }
        else{ lex = deserializeLexicon(); }
        return lex;
    }

    @Override
    /**
     * Check, whether the given form or one of its allowed variant
     * is a known word form.
     * The allowed variants:
     *    - the form itself
     *    - the form without capitalized first character (for the forms at
     *      the beginning of the sentence)
     *    - the form completely lowercased if it was completely uppercased
     *    - the form lowercased but with capital first letter if it was
     *      completely uppercased
     */
    public boolean contains(String form) {
        return 
        lexicon.contains(form) ||
        lexicon.contains(ILexicon.removeCapitalization(form)) ||
        lexicon.contains(ILexicon.toLowerCaseIfUpperCase(form)) ||
        lexicon.contains(ILexicon.toCapitalizedLowerCaseIfUpperCase(form));
    }
    
    @Override
    public boolean addForm(String form) {
        if (lexicon.contains(form)){
            return false;
        }
        else{
            lexicon.add(form);
            return true;
        }
    }

    /**
     * Parse the morfflex file and build a Trie
     * from the word forms contained in it.
     * @return the built Trie containing 
     * all the word forms present in the morfflex file
     * @see cz.cuni.mff.souradat.spellcheck.trie.Trie
     */
    private Trie loadMorfflex(){
        
	    Path morfflexPath = Path.of(morfflexFilename);

        try{
            // the word forms are in the 3rd column in the morfflex file
            Stream<String> forms = Files.lines(morfflexPath).map(line -> (line.split("\t")[2]));
            Trie trie = Trie.fromStream(forms);
            return trie;
        }
        catch(IOException e){
            throw new Error("Morfflex file not found. Please, check that the morfflex file was successfully download by using the dependencies in for the target `run`, and then start the shell again.");
        }
    }

    /**
     * Serialize to the given Trie lexicon, 
     * to the file specified by the attribut `serializedFilename`.
     * @param lexicon: lexicon to be serialized.
     */
    private void serializeLexicon(Trie lexicon){
        try(var oos = new ObjectOutputStream(new FileOutputStream(serializedFilename))){
            oos.writeObject(lexicon);
        }
        catch(IOException e){
            throw new Error(e);
        }
    }

    /**
     * Deserialize to the given Trie lexicon, 
     * from the file specified by the attribut `serializedFilename`.
     * @returns the deserialized lexicon
     */
    private Trie deserializeLexicon(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serializedFilename))){
            Trie lexicon = (Trie)ois.readObject();
            return lexicon;
        }
        catch(IOException e){
            throw new Error("Unsuccesful deserialization of te lexicon.");
        }
        catch(ClassNotFoundException e){
            throw new Error("Not safe class in deserialization.");
        }
    }


}
