package flustix.fluxifyed.modules.xp.utils;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatTask {
    public static void run() {
        if (canRun()) {
            Main.LOGGER.info("Running stat task...");

            StringBuilder query = new StringBuilder();

            for (XPGuild guild : XP.getGuilds().values()) {
                int i = 1;
                for (XPUser user : guild.getTop()) {
                    String userQuery = "INSERT INTO xpStats (guildid, userid, rank, xp, time) VALUES ('" + guild.getID() + "', '" + user.getID() + "', " + i + ", " + user.getXP() + ", '" + System.currentTimeMillis() + "');";
                    query.append(userQuery);
                    i++;
                }
            }

            Main.LOGGER.info("Collected all stats, executing query...");
            Database.executeQuery(query.toString());
            Main.LOGGER.info("Uploaded stats to database. Deleting stats older than 30 days...");
            Database.executeQuery("DELETE FROM xpStats WHERE time < " + (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)));
            Main.LOGGER.info("Done!");
        }
    }

    private static boolean canRun() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() == 0 && now.getMinute() == 0;
    }
}
