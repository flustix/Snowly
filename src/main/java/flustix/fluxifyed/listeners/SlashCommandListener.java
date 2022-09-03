package flustix.fluxifyed.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommandList;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommandList.execute(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        SlashCommandList.getCommands().forEach((key, command) -> {
            if (command.getName().equals(event.getName())) {
                command.getOptionAutocompletes().forEach((autoKey, autocompletes) -> {
                    if (event.getFocusedOption().getName().equals(autoKey)) {
                        List<Command.Choice> choices = new ArrayList<>();

                        for (String choice : autocompletes) {
                            if (choice.toLowerCase().startsWith(event.getOption(autoKey).getAsString().toLowerCase())) {
                                choices.add(new Command.Choice(choice, choice));
                            }
                        }

                        event.replyChoices(choices).complete();
                    }
                });
            }
        });
    }
}
