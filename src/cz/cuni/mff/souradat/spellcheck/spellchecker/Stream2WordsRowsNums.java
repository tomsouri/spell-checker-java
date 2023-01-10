package cz.cuni.mff.souradat.spellcheck.spellchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * Class for converting io stream to an iterable of row-numbered words.
 */
public class Stream2WordsRowsNums implements Iterable<WordWithRowNum> {

    private BufferedReader inputStream;

    public Stream2WordsRowsNums(BufferedReader inputStream){
        this.inputStream = inputStream;
    }

    /**
     * The default alphabet of known Czech characters,
     * used to determine whether a character is word character or not.
     */
    private static Set<Character> alphabet = new HashSet<Character>();
    static {
        for (char c = 'a'; c<= 'z'; c++){
            alphabet.add(c);
            alphabet.add(Character.toUpperCase(c));
        }
        for (char c: new char[]{'á', 'é', 'í', 'ý', 'ó', 'ú', 'ů', 'ž', 'š', 'č', 'ř', 'ď', 'ť', 'ň', 'ě'}){
            alphabet.add(c);
            alphabet.add(Character.toUpperCase(c));
        }
    }
    
    /**
     * Determines, whether the given character is a word character.
     * @param c: the character to be checked.
     * @return true if the character is a word character, false otherwise
     */
    private static boolean isWordCharacter(char c){
        return (alphabet.contains(c));
    }

    @Override
    /**
     * During iteration reads the whole io stream,
     * buffers the words and counts the row number.
     */
    public Iterator<WordWithRowNum> iterator() {
        return new Iterator<WordWithRowNum>(){
            private int rowNum = 1;

            private WordWithRowNum getNextWord() {
                StringBuilder builder = new StringBuilder();
                int c;
                try {
                    while ((c = inputStream.read()) != -1){
                        char ch = (char) c;
                        if (!isWordCharacter(ch)){
                            if (builder.length()!=0){
                                WordWithRowNum result = new WordWithRowNum(builder.toString(), rowNum);
                                if (c == '\n'){
                                    rowNum++;
                                }
                                return result;
                            }
                        }
                        else{
                            builder.append(ch);
                        }
                        if (c == '\n'){
                            rowNum++;
                        }

                    }
                    if (builder.length()!=0){
                        return new WordWithRowNum(builder.toString(), rowNum);
                    }
                    return null;
                }
                catch(IOException e){
                    return null;
                }
            }

            private WordWithRowNum bufferedWord = getNextWord();

            @Override
            public boolean hasNext() {
                return bufferedWord != null;
            }

            @Override
            public WordWithRowNum next() {
                WordWithRowNum toReturn = bufferedWord;
                bufferedWord = getNextWord();
                return toReturn;
            }};
    }
}