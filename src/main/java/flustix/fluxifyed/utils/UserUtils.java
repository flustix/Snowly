package flustix.fluxifyed.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class UserUtils {
    public static void directMessage(Message message, MessageCreateData data) {
        directMessage(message.getAuthor(), data);
    }

    public static void directMessage(User user, MessageCreateData data) {
        user.openPrivateChannel().complete().sendMessage(data).queue();
    }
}
