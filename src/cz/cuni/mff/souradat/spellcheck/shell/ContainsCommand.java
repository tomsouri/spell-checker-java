package cz.cuni.mff.souradat.spellcheck.shell;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class ContainsCommand implements ICommand {
    private static final String name = "has";
    private static final String help = "Checks, whether a given form is in the lexion or not.";
    private static final String usage = "has předivnéslovo";

    private SpellChecker spellChecker;

    public ContainsCommand(SpellChecker spellChecker) {
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
    /**Executes the contains command.
     * Calls the SpellChecker method contains(form).
     * @return true/false
     * @see cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker
     */
    public String execute(String... params) {
        if (params.length != 1){
            return "Unsuccessful. Expected exactly one parameter.\n Example usage of this command is: " + getUsage();
        }
        String form = params[0];
        boolean succ = spellChecker.contains(form);
        return Boolean.toString(succ);

    }

    @Override
    public String getHelpText() {
        return help;
    }


}