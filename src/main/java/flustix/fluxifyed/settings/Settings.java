package flustix.fluxifyed.settings;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    static final Map<String, GuildSettings> guilds = new HashMap<>();
    static final Map<String, UserSettings> users = new HashMap<>();

    public static void loadGuild(Guild g) {
        ResultSet rs = Database.executeQuery("SELECT * FROM guilds WHERE guildid = " + g.getId());
        if (rs == null) return;

        try {
            while (rs.next()) {
                guilds.put(g.getId(), new GuildSettings(rs.getString("guildid")));
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading guild settings for guild '" + g.getName() + "' (" + g.getId() + ")", e);
        }
    }

    public static GuildSettings getGuildSettings(String guildId) {
        GuildSettings guild = guilds.get(guildId);

        if (guild == null)
            guild = new GuildSettings(guildId);

        return guild;
    }

    public static UserSettings getUserSettings(String userId) {
        UserSettings user = users.get(userId);

        if (user == null) {
            user = new UserSettings(userId);
            users.put(userId, user);
        }

        return user;
    }

    public static boolean hasSettings(String id) {
        return guilds.containsKey(id) || users.containsKey(id);
    }
}
