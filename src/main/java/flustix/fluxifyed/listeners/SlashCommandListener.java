package flustix.fluxifyed.listeners;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.components.SlashCommandList;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommandList.execute(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        SlashCommand command = SlashCommandList.getCommand(event.getName());
        String option = event.getFocusedOption().getName();

        OptionMapping optionMapping = event.getOption(option);
        if (optionMapping == null) return; // shouldn't that return "" if its empty?
        String input = optionMapping.getAsString();

        event.replyChoices(command.handleAutocomplete(option, input)).complete();
    }
}
