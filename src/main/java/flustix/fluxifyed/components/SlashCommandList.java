package flustix.fluxifyed.components;

import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.localization.Localization;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
            moduleCommandMap.put(command.getName(), command);
            commandMap.put(command.getName(), moduleID);
        }

        commands.put(moduleID, moduleCommandMap);
    }

    public static void registerCommands(ReadyEvent event) {
        List<CommandData> slashCommandList = new ArrayList<>();

        commands.forEach((module, list) -> list.forEach((name, command) -> {
            String descriptionKey = "command.description." + module + "." + name;

            SlashCommandData data = Commands.slash(name, Localization.get(descriptionKey));
            data.addOptions(command.getOptions());
            data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));
            data.setGuildOnly(command.isGuildOnly());
            data.setDescriptionLocalizations(Localization.getAll(descriptionKey));

            slashCommandList.add(data);
        }));

        event.getJDA().updateCommands().addCommands(slashCommandList).complete();
    }

    public static void execute(SlashCommandInteractionEvent interaction) {
        try {String moduleID = commandMap.get(interaction.getName());

            if (commands.containsKey(moduleID)) {
                if (!commands.get(moduleID).containsKey(interaction.getName())) {
                    interaction.reply("This command is not implemented!").setEphemeral(true).queue();
                    return;
                }

                SlashCommand command = commands.get(moduleID).get(interaction.getName());

                // check if the bot has the required permissions
                Guild guild = interaction.getGuild();
                if (guild != null && !guild.getSelfMember().hasPermission(command.getBotPermissions())) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Missing Permissions")
                            .setDescription("The bot is missing the following permissions: " + command.getBotPermissions().toString())
                            .setColor(Colors.ERROR);

                    interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
                    return;
                }

                command.execute(interaction);
            } else
                interaction.reply("This command is not implemented!").setEphemeral(true).queue();
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("An error occurred!")
                    .setDescription("Please report this to the developer!")
                    .setColor(Colors.ERROR);

            StringBuilder stackTrace = new StringBuilder(e + "\n");
            for (StackTraceElement element : e.getStackTrace())
                stackTrace.append("    ").append(element.toString()).append("\n");

            if (stackTrace.length() > 1000)
                stackTrace = new StringBuilder(stackTrace.substring(0, 997)).append("...");

            embed.addField("Stack Trace", "```" + stackTrace + "```", false);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }

    }

    public static SlashCommand getCommand(String commandName) {
        if (commandMap.containsKey(commandName)) {
            String moduleID = commandMap.get(commandName);
            return commands.get(moduleID).get(commandName);
        }

        return new SlashCommand("", false);
    }

    public static HashMap<String, String> getCommandMap() {
        return commandMap;
    }

    public static TreeMap<String, TreeMap<String, SlashCommand>> getCommands() {
        return commands;
    }
}
