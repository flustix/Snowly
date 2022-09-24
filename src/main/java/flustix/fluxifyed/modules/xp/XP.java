package flustix.fluxifyed.modules.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Random;

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

        if (!Settings.getGuildSettings(event.getGuild().getId()).xpEnabled()) return;

        XPUser user = guild.getUser(event.getAuthor().getId());
        int xp = new Random().nextInt(11) + 10;
        user.addXP(xp, event);
    }

    public static void initGuild(Guild newGuild) {
        try {
            XPGuild guild = new XPGuild(newGuild.getId());

            ResultSet users = Database.executeQuery("SELECT * FROM xp WHERE guildid = '" + newGuild.getId() + "'");

            while (users.next()) {
                XPUser user = new XPUser(newGuild.getId(), users.getString("userid"));
                user.setXP(users.getInt("xp"));
                guild.addUser(user);
            }

            guilds.put(newGuild.getId(), guild);
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading xp for guild  '" + newGuild.getName() + "' (" + newGuild.getId() + ")", e);
        }
    }

    public static XPGuild getGuild(String guildID) {
        return guilds.get(guildID);
    }

    public static HashMap<String, XPGuild> getGuilds() {
        return guilds;
    }
}
