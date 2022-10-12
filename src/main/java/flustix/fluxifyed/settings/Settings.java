package flustix.fluxifyed.settings;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Settings {
    static final Map<String, GuildSettings> guilds = new HashMap<>();
    static final Map<String, UserSettings> users = new HashMap<>();

    public static void loadGuild(Guild g) {
        ResultSet rs = Database.executeQuery("SELECT * FROM guilds WHERE guildid = " + g.getId());
        if (rs == null) return;

        GuildSettings guild = null;

        try {
            while (rs.next()) {
                guild = new GuildSettings(rs.getString("guildid"));

                for (Module module : Main.getModules()) {
                    try {
                        guild.setModuleEnabled(module.id, rs.getBoolean(module.id + "Module"));
                    } catch (Exception ignored) {}
                }

                guilds.put(g.getId(), guild);
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading guild settings for guild '" + g.getName() + "' (" + g.getId() + ")", e);
        }

        if (guild == null) {
            guild = new GuildSettings(g.getId());
            guild.setup();
            guilds.put(g.getId(), guild);
        }
    }

    public static GuildSettings getGuildSettings(String guildId) {
        GuildSettings guild = guilds.get(guildId);

        if (guild == null) {
            guild = new GuildSettings(guildId);
            guild.setup();
        }

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
}
