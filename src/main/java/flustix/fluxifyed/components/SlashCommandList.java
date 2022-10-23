package flustix.fluxifyed.components;

import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class SlashCommandList {
    private static final TreeMap<String, TreeMap<String, SlashCommand>> commands = new TreeMap<>();
    private static final HashMap<String, String> commandMap = new HashMap<>(); // <command name, module id>

    public static void addModuleCommands(String moduleID, List<SlashCommand> moduleCommands) {
        TreeMap<String, SlashCommand> moduleCommandMap = new TreeMap<>();

        for (SlashCommand command : moduleCommands) {
            moduleCommandMap.put(command.name, command);
            commandMap.put(command.name, moduleID);
        }

        commands.put(moduleID, moduleCommandMap);
    }

    public static void registerCommands(ReadyEvent event) {
        List<CommandData> slashCommandList = new ArrayList<>();

        commands.forEach((module, list) -> list.forEach((name, command) -> {
            SlashCommandData commandData = Commands.slash(name, command.getDescription());
            commandData.addOptions(command.getOptions());
            commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));
            commandData.setGuildOnly(command.isGuildOnly());
            slashCommandList.add(commandData);
        }));

        event.getJDA().updateCommands().addCommands(slashCommandList).complete();
    }

    public static void execute(SlashCommandInteractionEvent interaction) {
        String moduleID = commandMap.get(interaction.getName());

        if (commands.containsKey(moduleID)) {
            if (!commands.get(moduleID).containsKey(interaction.getName())) {
                SlashCommandUtils.replyEphemeral(interaction, "This command is not implemented yet.");
                return;
            }

            SlashCommand command = commands.get(moduleID).get(interaction.getName());
            command.execute(interaction);
        } else
            SlashCommandUtils.replyEphemeral(interaction, "This command is not implemented yet.");
    }

    public static SlashCommand getCommand(String commandName) {
        if (commandMap.containsKey(commandName)) {
            String moduleID = commandMap.get(commandName);
            return commands.get(moduleID).get(commandName);
        }

        return new SlashCommand("", "");
    }

    public static HashMap<String, String> getCommandMap() {
        return commandMap;
    }

    public static TreeMap<String, TreeMap<String, SlashCommand>> getCommands() {
        return commands;
    }
}
