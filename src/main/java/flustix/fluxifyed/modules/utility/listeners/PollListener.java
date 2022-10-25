package flustix.fluxifyed.modules.utility.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.utility.components.PollInstance;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PollListener extends ListenerAdapter {
    private static final HashMap<String, PollInstance> polls = new HashMap<>();

    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("poll:")) {
            PollInstance poll = polls.get(event.getMessageId());
            if (poll == null) {
                event.reply("This poll has expired!").setEphemeral(true).queue();
                return;
            }

            if (event.getComponentId().equals("poll:end")) {
                Member member = event.getMember();
                if (member == null) {
                    Main.LOGGER.warn("Guild member intent is disabled!");
                    return;
                }

                if (event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                    poll.end(event);
                    polls.remove(event.getMessageId());
                } else {
                    event.reply("You don't have permission to end this poll!").setEphemeral(true).queue();
                }
                return;
            }

            poll.vote(event);
        }
    }

    public static void addPoll(String messageId, PollInstance poll) {
        polls.put(messageId, poll);
    }
}
