package flustix.fluxifyed.components;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SlashCommand {
    final String name;
    final String description;
    final List<Permission> requiredPermissions = new ArrayList<>();
    /**
     * The permissions required for the bot to execute the command.
     */
    final List<Permission> botPermissions = new ArrayList<>();
    final List<OptionData> options = new ArrayList<>();
    final HashMap<String, List<Autocomplete>> optionAutocompletes = new HashMap<>();
    final boolean guildOnly;

    public SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
        guildOnly = false;
    }

    public SlashCommand(String name, String desc, boolean guildOnly) {
        this.name = name;
        this.description = desc;
        this.guildOnly = guildOnly;
    }

    public void addOption(OptionType type, String name, String description, boolean required, boolean autocomplete) {
        options.add(new OptionData(type, name, description, required, autocomplete));
    }

    public void addAutocomplete(String option, Autocomplete... autocompletes) {
        if (optionAutocompletes.containsKey(option)) {
            for (Autocomplete autocomplete : autocompletes) {
                for (Autocomplete existingAutocomplete : optionAutocompletes.get(option)) {
                    if (!existingAutocomplete.getName().equals(autocomplete.getName())) {
                        optionAutocompletes.get(option).add(autocomplete);
                    }
                }
            }
        } else {
            List<Autocomplete> list = new ArrayList<>(Arrays.asList(autocompletes));
            optionAutocompletes.put(option, list);
        }
    }

    public void addPermissions(Permission... perm) {
        requiredPermissions.addAll(Arrays.asList(perm));
    }

    public void addBotPermissions(Permission... perm) {
        botPermissions.addAll(Arrays.asList(perm));
    }

    public void execute(SlashCommandInteraction interaction) {
        interaction.reply("This command is not implemented yet.").complete();
    }

    public List<Command.Choice> handleAutocomplete(CommandAutoCompleteInteractionEvent event, String option, String input) {
        List<Command.Choice> choices = new ArrayList<>();

        for (Autocomplete choice : optionAutocompletes.getOrDefault(option, new ArrayList<>())) {
            if (choice.getValue().toLowerCase().startsWith(input.toLowerCase())) {
                choices.add(new Command.Choice(choice.getName(), choice.getValue()));
            }
        }

        return choices;
    }

    public String getName() {
        return name;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public String getDescription() {
        return description;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    public List<Permission> getPermissions() {
        return requiredPermissions;
    }

    public List<Permission> getBotPermissions() {
        return botPermissions;
    }

    public HashMap<String, List<Autocomplete>> getOptionAutocompletes() {
        return optionAutocompletes;
    }
}
