package cz.cuni.mff.souradat.spellcheck.shell;

public interface ICommand {
    /**Get string representing the command.
     * @return string representation of the command
     */
    String getName();

    /**Executes the command.
     * @param params: the parameters for the command
     * @return the output of the command
     */
    String execute(String ... params);

    /** Get the help text explaining the command.
     * @return the help text.
     */
    String getHelpText();

    /**Get text containing an example usage of the command.
     * @return the example usage text
     */
    String getUsage();
}