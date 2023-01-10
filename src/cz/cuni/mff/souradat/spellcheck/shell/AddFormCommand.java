package cz.cuni.mff.souradat.spellcheck.shell;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class AddFormCommand implements ICommand {
    private static final String name = "add";
    private static final String help = "Adds the given form to the lexicon of known words.";
    private static final String usage = "add novéslovíčko";

    private SpellChecker spellChecker;

    public AddFormCommand(SpellChecker spellChecker) {
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
    /**Executes the add command.
     * Calls the SpellChecker method addForm(form).
     * @return message about success of adding the form
     * @see cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker
     */
    public String execute(String... params) {
        if (params.length != 1){
            return "Unsuccessful. Expected exactly one parameter.\n Example usage of this command is: " + getUsage();
        }
        String form = params[0];
        boolean succ = spellChecker.addForm(form);
        if (succ){
            return String.format("The form %s was successfully added to the lexicon of known forms.", form);
        }
        else{
            return String.format("The lexicon already contains the form %s.", form);
        }

    }

    @Override
    public String getHelpText() {
        return help;
    }


}