package flustix.fluxifyed.modules.reactionroles.listeners;

import flustix.fluxifyed.modules.reactionroles.ReactRoles;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        ReactRoles.onReactAdd(event);
    }

    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        ReactRoles.onReactRemove(event);
    }
}
