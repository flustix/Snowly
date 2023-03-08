package flustix.fluxifyed.modules.moderation.automod.components.messages;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A runtime storage for every message sent, ever.
 * Mostly used by AutoMod for spam detection and such.
 */
public class MessageStorage {
    private static final HashMap<String, Message> byID = new HashMap<>(); // by message ID
    private static final HashMap<String, List<Message>> byAuthor = new HashMap<>(); // by author ID

    public static void addMessage(Message message) {
        byID.put(message.getId(), message);

        List<Message> messages = byAuthor.computeIfAbsent(message.getAuthor().getId(), k -> new ArrayList<>());
        messages.add(message);
    }

    public static Message getByID(String id) {
        return byID.get(id);
    }

    public static List<Message> getByAuthor(String id) {
        if (!byAuthor.containsKey(id)) return new ArrayList<>();
        return byAuthor.get(id).stream().toList();
    }

    public static List<Message> getByAuthorInGuild(String id, String guildID) {
        if (!byAuthor.containsKey(id)) return new ArrayList<>();
        return byAuthor.get(id).stream().filter(m -> m.getGuild().getId().equals(guildID)).toList();
    }
}
