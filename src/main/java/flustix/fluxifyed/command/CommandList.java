package flustix.fluxifyed.command;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.messages.MessageUtils;
import flustix.fluxifyed.utils.permissions.PermissionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.TreeMap;

public class CommandList {
    private static final TreeMap<String, Command> commands = new TreeMap<>();

    public static void initializeList() {
    }

    private static void addCommand(Command command) {
        commands.put(command.getName(), command);
        Main.LOGGER.info("Loaded command " + command.getName() + "!");
    }

    private static Command getCommand(String name) {
        return commands.get(name);
    }

    public static TreeMap<String, Command> getCommands() {
        return commands;
    }

    public static void onMessage(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        if (raw.toLowerCase().startsWith(Main.getPrefix())) {
            raw = raw.substring(Main.getPrefix().length());
            String[] parts = raw.split(" ");
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            String commandName = parts[0].toLowerCase();

            Command command = getCommand(commandName);

            // command does not exist
            if (command == null)
                return;

            try {
                if (PermissionUtils.checkLevel(event.getMember(), command.getPermissionLevel())) {
                    command.execute(event, args);
                } else {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("You don't have enough permissions to run this command!")
                            .addField("Required Permissions", PermissionUtils.getDescription(command.getPermissionLevel()), false)
                            .setColor(0xFF5555);

                    MessageUtils.reply(event, embed.build());
                }
            } catch (Exception e) {
                MessageUtils.reply(event, "Something went wrong executing the command: " + e);
            }
        }
    }
}
