package flustix.fluxifyed.utils.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageUtils {
    public static Message reply(MessageReceivedEvent event, String content) {
        return event.getMessage().reply(content).mentionRepliedUser(false).complete();
    }

    public static Message reply(MessageReceivedEvent event, MessageEmbed embed) {
        return event.getMessage().replyEmbeds(embed).mentionRepliedUser(false).complete();
    }

    public static Message reply(MessageReceivedEvent event, Message message) {
        return event.getMessage().reply(message).mentionRepliedUser(false).complete();
    }
}
