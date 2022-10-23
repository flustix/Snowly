package flustix.fluxifyed.components;

import net.dv8tion.jda.api.Permission;
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
    final List<OptionData> options = new ArrayList<>();
    final HashMap<String, List<String>> optionAutocompletes = new HashMap<>();

    public SlashCommand(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public void addOption(OptionType type, String name, String description, boolean required, boolean autocomplete) {
        options.add(new OptionData(type, name, description, required, autocomplete));
    }

    public void addAutocomplete(String option, String... autocompletes) {
        if (optionAutocompletes.containsKey(option)) {
            for (String autocomplete : autocompletes) {
                if (!optionAutocompletes.get(option).contains(autocomplete)) {
                    optionAutocompletes.get(option).add(autocomplete);
                }
            }
        } else {
            List<String> list = new ArrayList<>(Arrays.asList(autocompletes));
            optionAutocompletes.put(option, list);
        }
    }

    public void addPermissions(Permission... perm) {
        requiredPermissions.addAll(Arrays.asList(perm));
    }

    public void execute(SlashCommandInteraction interaction) {
        interaction.reply("This command is not implemented yet.").complete();
    }

    public List<Command.Choice> handleAutocomplete(String option, String input) {
        List<Command.Choice> choices = new ArrayList<>();

        for (String choice : optionAutocompletes.getOrDefault(option, new ArrayList<>())) {
            if (choice.toLowerCase().startsWith(input.toLowerCase())) {
                choices.add(new Command.Choice(choice, choice));
            }
        }

        return choices;
    }

    public String getName() {
        return name;
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

    public HashMap<String, List<String>> getOptionAutocompletes() {
        return optionAutocompletes;
    }
}
