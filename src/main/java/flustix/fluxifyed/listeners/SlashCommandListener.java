package flustix.fluxifyed.listeners;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.components.SlashCommandList;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommandList.execute(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        SlashCommand command = SlashCommandList.getCommand(event.getName());
        String option = event.getFocusedOption().getName();
        String input = Objects.requireNonNull(event.getOption(option)).getAsString();

        event.replyChoices(command.handleAutocomplete(option, input)).complete();
    }
}
