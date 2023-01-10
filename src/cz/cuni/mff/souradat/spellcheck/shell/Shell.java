package cz.cuni.mff.souradat.spellcheck.shell;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import cz.cuni.mff.souradat.spellcheck.spellchecker.SpellChecker;

/** Represents (non)interactive shell.
 */
public class Shell {
    private ArrayList<ICommand> commands = new ArrayList<ICommand>();
    private SpellChecker spellChecker;

    public Shell(){
        spellChecker = new SpellChecker();
        commands.add(new HelpCommand(commands, spellChecker));
        commands.add(new ExitCommand(spellChecker));
        commands.add(new CheckCommand(spellChecker));
        commands.add(new CorrectCommand(spellChecker));
        commands.add(new AddFormCommand(spellChecker));
        commands.add(new AlterCommand(spellChecker));
        commands.add(new ContainsCommand(spellChecker));  
    }

    /** Processes one line containing
     *  command and arguments.
     * @param line: one line from the shell.
     * @return output of the run command.
     */
    private String processLine(String line){
        line = line.trim();
        if ("".equals(line)){
            // empty line, do nothing
            return null;
        }

        String[] tokens = line.split("\\s+");
        String command = tokens[0];
        ICommand cmd = getCommand(command);
        if(cmd != null){
            String[] cmsArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
            String result = cmd.execute(cmsArgs);
            if (result!=null){
                return result;
            }
            else {return "";}
        }
        else{
            return "Unknown command";
        }
    }

    /** Runs the interactive shell.
     * Reads all lines from the console/stdin,
     * and executes corresponding commands.
     * Prints the results of the commands
     * to the console/stdout.
     * 
     * @see cz.cuni.mff.souradat.spellcheck.shell.ICommand
     */
    public void run(){
        // TODO: otestovat, ze to dobre bezi i spolu s checkerem.
        Console console = System.console();
        if (console == null){
            try(BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))){
                System.out.println(commands.get(0).execute());

                String line;
    
                while ((line = stdin.readLine()) != null){
                    String result = processLine(line);
                    if (result == null){continue;}
                    else{
                        System.out.println(result);
                    }
                    /* /
                    line = line.trim();
                    if ("".equals(line)){
                        // empty line, do nothing
                        continue;
                    }
    
                    String[] tokens = line.split("\\s+");
                    String command = tokens[0];
                    ICommand cmd = getCommand(command);
                    if(cmd != null){
                        String[] cmsArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                        String result = cmd.execute(cmsArgs);
                        if (result!=null){
                            System.out.println(result);
                        }
                    }
                    else{
                        System.out.println("Unknown command");
                    }
                    /* */
                }
            }
            catch(IOException e){}
        }
        else{
            console.printf(commands.get(0).execute() + "\n");
            while(true){
                String line = console.readLine("> ");
                String result = processLine(line);
                if (result == null){continue;}
                    else{
                        console.printf(result + "\n");
                    }
                /* /
                line = line.trim();
                if ("".equals(line)){
                    // empty line, do nothing
                    continue;
                }
    
                String[] tokens = line.split("\\s+");
                String command = tokens[0];
                ICommand cmd = getCommand(command);
                if(cmd != null){
                    String[] cmsArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                    String result = cmd.execute(cmsArgs);
                    if (result!=null){
                        console.printf(result + "\n");
                    }
                }
                else{
                    console.printf("Unknown command\n");
                }
                /* */
            }

        }
        /*
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String line = "";

            while ((line = stdin.readLine()) != null){
            // Do something.
            submittedString += line + '\n';
         * 
         */
        /* /
        try(BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))){
            String line;
            System.out.print("> ");

            while ((line = stdin.readLine()) != null){
                line = line.trim();
                if ("".equals(line)){
                    // empty line, do nothing
                    continue;
                }

                String[] tokens = line.split("\\s+");
                String command = tokens[0];
                ICommand cmd = getCommand(command);
                if(cmd != null){
                    String[] cmsArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                    String result = cmd.execute(cmsArgs);
                    if (result!=null){
                        System.out.println(result);
                    }
                }
                else{
                    System.out.println("Unknown command");
                }
                System.out.print("> ");
            }
            System.out.println();
        }
        catch(IOException e){}
        /* */
        /* /
        Console console = System.console();
        if (console == null){
            System.out.println("No console available");
            System.exit(1);
        }

        
        // print help message:
        System.out.println(commands.get(0).execute());

        while(true){
            String line = console.readLine("> ");
            line = line.trim();
            if ("".equals(line)){
                // empty line, do nothing
                continue;
            }

            String[] tokens = line.split("\\s+");
            String command = tokens[0];
            ICommand cmd = getCommand(command);
            if(cmd != null){
                String[] cmsArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                String result = cmd.execute(cmsArgs);
                if (result!=null){
                    console.printf(result + "\n");
                }
            }
            else{
                console.printf("Unknown command\n");
            }
        }
        /* */
    }

    /**
     * Finds ICommand corresponding to a given string representation.
     * @param com: string representing a command
     * @return correconding ICommand
     */
    private ICommand getCommand(String com){
        for (ICommand command: commands){
            if (command.getName().equals(com)){
                return command;
            }
        }
        return null;
    }
}