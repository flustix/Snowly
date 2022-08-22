package flustix.fluxifyed.listeners;

import flustix.fluxifyed.command.CommandList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
//        CommandList.onMessage(event); message intent is off lmao
    }
}
