package cz.cuni.mff.souradat.spellcheck.lexicon;


import java.util.*;

/** Class for computing Levenshtein distance of 2 strings.
 * Heavily based on https://www.geeksforgeeks.org/java-program-to-implement-levenshtein-distance-computing-algorithm/
 */
public class Levenshtein {
    /**Computes Levenshtein distance of 2 strings.
     * Uses dynamic programming.
     * @param str1: the first string
     * @param str2: the second string
     * @return the Levenshtein distance of the given strings
     */
    public static int distance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++){
            for (int j = 0; j <= str2.length(); j++) {

                // If str1 is empty, all characters of
                // str2 are inserted into str1, which is of
                // the only possible method of conversion
                // with minimum operations.
                if (i == 0) {
                    dp[i][j] = j;
                }

                // If str2 is empty, all characters of str1
                // are removed, which is the only possible
                //  method of conversion with minimum
                //  operations.
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    // find the minimum among three
                    // operations below

                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                    + NumOfReplacement(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
                    dp[i - 1][j] + 1, // delete
                    dp[i][j - 1] + 1); // insert
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    /**Get number of replacements needed
     *  to get the first character 
     * from the second one. 
     * @param c1: the first character
     * @param c2: the second character
     * @return 0 if the same, 1 otherwise
     */
    static int NumOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    /**Get the minimal value from a variable amount of numbers.
     * @param nums: the numbers amongst which we seek the minimum
     * @return the minimum from given numbers or max_value if no number is given
     */
    static int minm_edits(int... nums){

    return Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
    }

}