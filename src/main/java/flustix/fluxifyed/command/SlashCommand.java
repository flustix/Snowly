package flustix.fluxifyed.command;

import flustix.fluxifyed.utils.permissions.PermissionLevel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlashCommand {
    String name;
    String description;
    PermissionLevel permissionLevel = PermissionLevel.EVERYONE;
    List<OptionData> options = new ArrayList<>();
    HashMap<String, String[]> optionAutocompletes = new HashMap<>();

    public SlashCommand(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public void addOption(OptionType type, String name, String description, boolean required, boolean autocomplete) {
        options.add(new OptionData(type, name, description, required, autocomplete));
    }

    public void addAutocomplete(String option, String... autocompletes) {
        optionAutocompletes.put(option, autocompletes);
    }

    public void setPermissionLevel(PermissionLevel level) {
        this.permissionLevel = level;
    }

    public void execute(SlashCommandInteraction interaction) {
        interaction.reply("This command is not implemented yet.").complete();
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

    public HashMap<String, String[]> getOptionAutocompletes() {
        return optionAutocompletes;
    }
}
