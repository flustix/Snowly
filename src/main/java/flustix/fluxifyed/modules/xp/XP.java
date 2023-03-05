package flustix.fluxifyed.modules.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.xp.components.XPChannel;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static void initGuild(Guild guild) {
        try {
            XPGuild xpGuild = new XPGuild(guild.getId());

            initUsers(guild, xpGuild);
            initRoles(guild, xpGuild);
            initChannels(guild, xpGuild);

            guilds.put(guild.getId(), xpGuild);
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading xp for guild  '" + guild.getName() + "' (" + guild.getId() + ")", e);
        }
    }

    static void initUsers(Guild guild, XPGuild xpGuild) throws Exception {
        ResultSet users = Database.executeQuery("SELECT * FROM fluxifyed.xp WHERE guildid = '" + guild.getId() + "'");

        if (users == null) {
            Main.LOGGER.warn("No users found for guild " + guild.getId());
            return;
        }

        while (users.next()) {
            XPUser user = new XPUser(xpGuild, users.getString("userid"));
            user.setXP(users.getInt("xp"));
            xpGuild.addUser(user);
        }
    }

    static void initRoles(Guild newGuild, XPGuild guild) throws Exception {
        ResultSet roles = Database.executeQuery("SELECT * FROM fluxifyed.xpRoles WHERE guildid = '" + newGuild.getId() + "'");

        if (roles == null)
            return;

        while (roles.next()) {
            String type = roles.getString("type");
            XPRole role = new XPRole(roles.getString("roleid"), roles.getFloat("value"));

            switch (type) {
                case "level" -> guild.addLevelRole(role);
                case "multiplier" -> guild.addMultiplier(role);
            }
        }
    }

    private static void initChannels(Guild guild, XPGuild xpGuild) throws SQLException {
        ResultSet channels = Database.executeQuery("SELECT * FROM fluxifyed.xpChannels WHERE guildid = '" + guild.getId() + "'");

        if (channels == null) return;

        while (channels.next()) {
            XPChannel channel = new XPChannel(channels.getString("channelid"), channels.getFloat("value"));
            xpGuild.addChannelMultiplier(channel);
        }
    }

    public static XPGuild getGuild(String guildID) {
        return guilds.get(guildID);
    }

    public static HashMap<String, XPGuild> getGuilds() {
        return guilds;
    }
}
