package flustix.fluxifyed.modules.xp.listeners;

import flustix.fluxifyed.modules.xp.XP;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        XP.addXP(event);
    }
}
