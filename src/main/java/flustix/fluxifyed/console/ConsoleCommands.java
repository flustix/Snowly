package flustix.fluxifyed.console;

import flustix.fluxifyed.console.commands.HelpConsoleCommand;
import flustix.fluxifyed.console.commands.StopConsoleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;

public class ConsoleCommands {
    static HashMap<String, ConsoleCommand> commands = new HashMap<>();
    public static Logger LOGGER = LoggerFactory.getLogger("Flux Console");

    public static void start() {
        registerCommand(new StopConsoleCommand());
        registerCommand(new HelpConsoleCommand());

        while (true) {
            listen();
        }
    }

    static void registerCommand(ConsoleCommand command) {
        commands.put(command.name, command);
        LOGGER.debug("Registered console command " + command.name);
    }

    public static void listen() {
        try {
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
            String line = reader.readLine();
            execute(line);
        } catch (Exception ex) {
            LOGGER.error("Error while reading console input: " + ex.getMessage(), ex);
        }
    }

    static void execute(String line) {
        String[] args = line.split(" ");

        if (args.length == 0) {
            return;
        }

        String commandName = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        if (commands.containsKey(commandName)) {
            commands.get(commandName).execute(commandArgs);
        } else {
            LOGGER.error("Unknown command " + commandName);
        }
    }

    public static HashMap<String, ConsoleCommand> getCommands() {
        return commands;
    }
}
