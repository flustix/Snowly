package flustix.fluxifyed.modules.reactionroles;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.reactionroles.components.ReactionRoleMessage;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ReactionRoles {
    static Map<String, ReactionRoleMessage> messages = new HashMap<>();

    public static void loadMessages() {
        ResultSet rs = Database.executeQuery("SELECT * FROM reactionRoles");

        try {
            while (rs.next()) {
                String messageid = rs.getString("messageid");
                Main.LOGGER.info("Loading reaction roles for message " + messageid);
                messages.put(messageid, new ReactionRoleMessage(messageid, rs.getString("data")));
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading reaction roles", e);
        }
    }

    public static void addMessage(String messageid, String data) {
        Database.executeQuery("INSERT INTO reactionRoles (messageid, data) VALUES (" + messageid + ", '" + data + "')");
        messages.put(messageid, new ReactionRoleMessage(messageid, data));
    }

    public static void onReactionAdd(MessageReactionAddEvent event) {
        ReactionRoleMessage message = messages.get(event.getMessageId());
        if (message == null) return;

        message.onReactionAdd(event);
    }

    public static void onReactionRemove(MessageReactionRemoveEvent event) {
        ReactionRoleMessage message = messages.get(event.getMessageId());
        if (message == null) return;

        message.onReactionRemove(event);
    }

    public static Map<String, ReactionRoleMessage> getMessages() {
        return messages;
    }

    public static ReactionRoleMessage getMessage(String messageid) {
        return messages.get(messageid);
    }
}
