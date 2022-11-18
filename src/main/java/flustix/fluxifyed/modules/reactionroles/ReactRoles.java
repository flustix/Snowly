package flustix.fluxifyed.modules.reactionroles;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.reactionroles.components.ReactMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ReactRoles {
    private static final Map<String, ReactMessage> reactMessages = new HashMap<>();

    public static void loadGuild(Guild guild) {
        ResultSet rs = Database.executeQuery("SELECT * FROM reactionRoles WHERE guildid = '?'", guild.getId());
        if (rs == null) return;

        try {
            while (rs.next()) {
                ReactMessage reactMessage = new ReactMessage(JsonParser.parseString(rs.getString("data")).getAsJsonObject(), false);
                reactMessages.put(reactMessage.messageid, reactMessage);
                Main.LOGGER.info("Loaded reaction role message " + reactMessage.messageid + " for guild " + guild.getId());
            }
        } catch (Exception ex) {
            Main.LOGGER.error("Failed to load reaction roles for guild " + guild.getName(), ex);
        }
    }

    public static void addReactMessage(ReactMessage reactMessage) {
        reactMessages.put(reactMessage.messageid, reactMessage);
    }

    public static void onReactAdd(MessageReactionAddEvent event) {
        User user = event.getUser();
        if (user == null || user.isBot()) return;

        ReactMessage reactMessage = reactMessages.get(event.getMessageId());
        if (reactMessage == null) return;

        Main.LOGGER.info("Reacted to message " + event.getMessageId());

        reactMessage.roles.forEach(reactRole -> {
            if (reactRole.emoji.equals(extractID(event.getEmoji()))) {
                Member member = event.getMember();
                if (member == null) return;

                Role role = event.getGuild().getRoleById(reactRole.role);
                if (role == null) return;

                event.getGuild().addRoleToMember(member, role).queue();
            }
        });
    }

    public static void onReactRemove(MessageReactionRemoveEvent event) {
        User user = event.getUser();
        if (user == null || user.isBot()) return;

        ReactMessage reactMessage = reactMessages.get(event.getMessageId());
        if (reactMessage == null) return;

        Main.LOGGER.info("Removing reaction role for " + user.getName());

        reactMessage.roles.forEach(reactRole -> {
            if (reactRole.emoji.equals(extractID(event.getEmoji()))) {
                Member member = event.getMember();
                if (member == null) return;

                Role role = event.getGuild().getRoleById(reactRole.role);
                if (role == null) return;

                event.getGuild().removeRoleFromMember(member, role).queue();
            }
        });
    }

    private static String extractID(EmojiUnion emoji) {
        if (emoji.getType() == Emoji.Type.CUSTOM) {
            return emoji.asCustom().getId();
        } else if (emoji.getType() == Emoji.Type.UNICODE) {
            return emoji.asUnicode().getAsCodepoints();
        }
        return "";
    }
}
