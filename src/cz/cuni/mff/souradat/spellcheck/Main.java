package cz.cuni.mff.souradat.spellcheck;

import cz.cuni.mff.souradat.spellcheck.shell.Shell;

/** Main class of the SpellCheck project. 
 * To run the SpellCheck project, 
 * run this class as the main class.
 * Example: java -Xmx11g cz.cuni.mff.souradat.spellcheck.Main
*/
public class Main {
    /**
     * Main method of the SpellCheck project.
     * @param args are ignored
     */
    public static void main(String[] args){
        Shell shell = new Shell();
        shell.run();
    }
}
