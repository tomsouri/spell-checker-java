package cz.cuni.mff.souradat.spellcheck.shell;

import java.util.StringJoiner;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class AlterCommand implements ICommand {
    private SpellChecker spellChecker;
    private int defaultAlternationsCount = 5;

    public AlterCommand(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }
    private static final String name = "alter";
    private static final String help = "Suggest alternations of the given form. Find `n` closest known words.";
    private static final String usage = "alter divnoslovo 5";
    
    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String getName() {
        return name;
    }

    private String getNClosestWords(String word, int n){
        StringJoiner result = new StringJoiner("\n");
        int counter = 0;
        for (String alternation: spellChecker.alter(word)){
            counter++;
            result.add(alternation);
            if (counter >= n){
                break;
            }
        }
        return result.toString();
    }
    @Override
    /**Executes the alter command.
     * Calls the SpellChecker method alter(form)
     * @return listing of alternations
     * @see cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker
     */
    public String execute(String... params) {
        if (params.length == 1){
            String form = params[0];
            return getNClosestWords(form, defaultAlternationsCount);
        }
        else if (params.length == 2){
            String form = params[0];
            int alterCount;
            try{
                alterCount = Integer.parseInt(params[1]);
            }
            catch (NumberFormatException e){
                return "Unsuccessful. The second parameter should be an integer.";
            }
            
            return getNClosestWords(form, alterCount);
        }
        else{
            return "Unsuccessful. This command expects 1 or 2 arguments.\nThe example usage is: " + getUsage();
        }
    }

    @Override
    public String getHelpText() {
        return help;
    }

}