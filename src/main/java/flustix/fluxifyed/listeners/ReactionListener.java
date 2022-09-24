package flustix.fluxifyed.listeners;

import flustix.fluxifyed.modules.reactionroles.ReactionRoles;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        ReactionRoles.onReactionAdd(event);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        ReactionRoles.onReactionRemove(event);
    }
}
