package cz.cuni.mff.souradat.spellcheck.shell;

import java.util.List;
import java.util.StringJoiner;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

class HelpCommand implements ICommand {
    List<ICommand> commands;
    private SpellChecker spellChecker;

    public HelpCommand(List<ICommand> commands, SpellChecker spellChecker){
        this.commands = commands;
        this.spellChecker = spellChecker;
    }
    private static final String name = "help";
    private static final String help = "Print this help";
    private static final String usage = "help";
    
    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    /**Executes the help command.
     * @return the listing of available commands
     */
    public String execute(String... params) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Available commands are:");
        for (ICommand command: commands){
            joiner.add(String.format("%s: %s", command.getName(), command.getHelpText()));
            joiner.add(String.format("\tExample usage: %s", command.getUsage()));
        }
        return joiner.toString();
    }

    @Override
    public String getHelpText() {
        return help;
    }

}