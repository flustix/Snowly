package flustix.fluxifyed.console.commands;

import flustix.fluxifyed.console.ConsoleCommand;
import flustix.fluxifyed.console.ConsoleCommands;

public class HelpConsoleCommand extends ConsoleCommand {
    public HelpConsoleCommand() {
        super("help", "Shows this help message.");
    }

    @Override
    public void execute(String[] args) {
        ConsoleCommands.LOGGER.info("Available commands:");
        ConsoleCommands.getCommands().forEach((key, command) -> {
            ConsoleCommands.LOGGER.info(" - " + command.name + ": " + command.description);
        });
    }
}
