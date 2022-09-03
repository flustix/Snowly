package flustix.fluxifyed.console.commands;

import flustix.fluxifyed.console.ConsoleCommand;
import flustix.fluxifyed.console.ConsoleCommands;

public class StopConsoleCommand extends ConsoleCommand {
    public StopConsoleCommand() {
        super("stop", "Stops the bot.");
    }

    @Override
    public void execute(String[] args) {
        ConsoleCommands.LOGGER.info("Stopping bot...");
        System.exit(0);
    }
}
