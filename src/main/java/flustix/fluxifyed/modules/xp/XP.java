package flustix.fluxifyed.modules.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.util.HashMap;

public class XP {
    private static final HashMap<String, XPGuild> guilds = new HashMap<>();

    public static void addXP(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        XPGuild guild = guilds.get(event.getGuild().getId());

        if (guild == null) {
            Main.LOGGER.warn("Guild " + event.getGuild().getId() + " is not initialized!");
            return;
        }

        GuildSettings settings = Settings.getGuildSettings(event.getGuild().getId());
        if (!settings.getBoolean("xp.enabled", true)) return;

        XPUser user = guild.getUser(event.getAuthor().getId());
        user.addXP(event);
    }

    public static void initGuild(Guild newGuild) {
        try {
            XPGuild guild = new XPGuild(newGuild.getId());

            initUsers(newGuild, guild);
            initRoles(newGuild, guild);

            guilds.put(newGuild.getId(), guild);
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading xp for guild  '" + newGuild.getName() + "' (" + newGuild.getId() + ")", e);
        }
    }

    static void initUsers(Guild newGuild, XPGuild guild) throws Exception {
        ResultSet users = Database.executeQuery("SELECT * FROM xp WHERE guildid = '" + newGuild.getId() + "'");

        if (users == null) {
            Main.LOGGER.warn("No users found for guild " + newGuild.getId());
            return;
        }

        while (users.next()) {
            XPUser user = new XPUser(guild, users.getString("userid"));
            user.setXP(users.getInt("xp"));
            guild.addUser(user);
        }
    }

    static void initRoles(Guild newGuild, XPGuild guild) throws Exception {
        ResultSet roles = Database.executeQuery("SELECT * FROM xpRoles WHERE guildid = '" + newGuild.getId() + "'");

        if (roles == null)
            return;

        while (roles.next()) {
            String type = roles.getString("type");
            XPRole role = new XPRole(roles.getString("roleid"), roles.getInt("value"));

            switch (type) {
                case "level" -> guild.addLevelRole(role);
                case "multiplier" -> guild.addMultiplier(role);
            }
        }
    }

    public static XPGuild getGuild(String guildID) {
        return guilds.get(guildID);
    }

    public static HashMap<String, XPGuild> getGuilds() {
        return guilds;
    }
}
