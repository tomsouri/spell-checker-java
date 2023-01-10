package cz.cuni.mff.souradat.spellcheck.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class CheckCommand implements ICommand {
    private static final String name = "check";
    private static final String help = "Check the given file and report unknown words in the second file.";
    private static final String usage = "check inputFilename outputFilename";


    private SpellChecker spellChecker;

    public CheckCommand(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    /**Executes the check command.
     * Calls the SpellChecker method check(in,out) to check the file.
     * @return message about success of the checking the file
     * @see cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker
     */
    public String execute(String... params) {
        if (params.length != 2){
            return "Unsuccessful. This command expects exactly 2 arguments.\nThe example usage is: " + getUsage();
        }
        String inFilename = params[0];
        String outFilename = params[1];
        boolean succ = false;
        try(BufferedReader in = new BufferedReader(new FileReader(inFilename)); BufferedWriter out = new BufferedWriter(new FileWriter(outFilename))){
            succ = spellChecker.check(in, out);
        }
        catch(IOException e){
            return String.format("Problems with reading the file %s or with writing to the file %s. Please, check that the file %s exists and that you have permission to write to the file %s", inFilename, outFilename, inFilename, outFilename);
        }
        if(succ){
            return String.format("Successfully checked the file %s and reported the unknown words to the file %s", inFilename, outFilename);
        }
        else{
            return "Problem in checking the file.";
        }

    }

    @Override
    public String getHelpText() {
        return help;
    }


}