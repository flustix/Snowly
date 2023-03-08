package flustix.fluxifyed.modules.moderation.listeners;

import flustix.fluxifyed.modules.moderation.automod.AutoMod;
import flustix.fluxifyed.modules.moderation.automod.components.messages.MessageStorage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.isWebhookMessage()) return;
        if (!event.isFromGuild()) return;

        MessageStorage.addMessage(event.getMessage());
        AutoMod.checkMessage(event.getMessage());
    }
}
