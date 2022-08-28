package flustix.fluxifyed.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.xp.types.XPGuild;
import flustix.fluxifyed.xp.types.XPUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Random;

public class XP {
    private static final HashMap<String, XPGuild> guilds = new HashMap<>();

    public static void addXP(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        XPGuild guild = guilds.get(event.getGuild().getId());

        if (guild == null) {
            Main.LOGGER.warn("Guild " + event.getGuild().getId() + " is not initialized!");
            return;
        }

        XPUser user = guild.getUser(event.getAuthor().getId());
        int xp = new Random().nextInt(11) + 10;
        user.addXP(xp);
    }

    public static void initGuild(String guildID) {
        Main.LOGGER.info("Initializing guild " + guildID);
        XPGuild guild = new XPGuild(guildID);

        try {
            ResultSet rs = Database.executeQuery("SELECT * FROM xp WHERE guildid = '" + guildID + "'");
            while (rs.next()) {
                XPUser user = new XPUser(guildID, rs.getString("userid"));
                user.addXP(rs.getInt("xp"));
                guild.addUser(user);
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while initializing guild " + guildID, e);
        }

        guilds.put(guildID, guild);
    }

    public static XPGuild getGuild(String guildID) {
        return guilds.get(guildID);
    }

    public static HashMap<String, XPGuild> getGuilds() {
        return guilds;
    }
}
