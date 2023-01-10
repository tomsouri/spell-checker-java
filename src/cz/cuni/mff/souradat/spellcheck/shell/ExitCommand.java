package cz.cuni.mff.souradat.spellcheck.shell;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class ExitCommand implements ICommand {
    private static final String name = "exit";
    private static final String help = "Exits the program";
    private static final String usage = "exit";

    private SpellChecker spellChecker;

    public ExitCommand(SpellChecker spellChecker) {
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
    /**Exits the program.
     */
    public String execute(String... params) {
        System.exit(0);
        return null;
    }

    @Override
    public String getHelpText() {
        return help;
    }


}