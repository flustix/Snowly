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
    private final String name;
    @Deprecated
    private String description = "";
    private final List<Permission> requiredPermissions = new ArrayList<>();
    /**
     * The permissions required for the bot to execute the command.
     */
    private final List<Permission> botPermissions = new ArrayList<>();
    private final List<OptionData> options = new ArrayList<>();
    private final HashMap<String, List<Autocomplete>> optionAutocompletes = new HashMap<>();
    private final boolean guildOnly;

    @Deprecated
    public SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
        guildOnly = false;
    }

    @Deprecated
    public SlashCommand(String name, String desc, boolean guildOnly) {
        this.name = name;
        this.description = desc;
        this.guildOnly = guildOnly;
    }

    public SlashCommand(String name, boolean guildOnly) {
        this.name = name;
        this.guildOnly = guildOnly;
    }

    public void addOption(OptionType type, String name, String description, boolean required, boolean autocomplete) {
        options.add(new OptionData(type, name, description, required, autocomplete));
    }

    public void addAutocomplete(String option, Autocomplete... autocompletes) {
        if (optionAutocompletes.containsKey(option)) {
            for (Autocomplete autocomplete1 : autocompletes) {
                boolean found = false;

                for (Autocomplete autocomplete : optionAutocompletes.get(option)) {
                    if (autocomplete.equals(autocomplete1)) {
                        found = true;
                        break;
                    }
                }

                if (!found) optionAutocompletes.get(option).add(autocomplete1);
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

    @Deprecated
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
