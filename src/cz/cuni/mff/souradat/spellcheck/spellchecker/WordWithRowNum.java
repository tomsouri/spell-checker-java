package cz.cuni.mff.souradat.spellcheck.spellchecker;

/**
 * Data class representing a word together with a row number (from the file),
 * on which the word occured.
 * *it should rather be a record and not a class.
 */
public class WordWithRowNum {
    public String word;
    public int rowNum;
    public WordWithRowNum(String word, int rowNum) {
        this.word = word;
        this.rowNum = rowNum;
    } 
}