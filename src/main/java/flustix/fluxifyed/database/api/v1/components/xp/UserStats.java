package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.v1.components.APIUser;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserStats {
    public APIUser user;
    public int level;
    public int xp;
    public int rank;
    public UserStatsGuild guild;
    public List<UserStatsChartEntry> chartEntries;

    public UserStats(XPUser xuser, XPGuild xguild) {
        this.level = XPUtils.calculateLevel(xuser.getXP());
        this.xp = xuser.getXP();
        this.rank = xguild.getTop().indexOf(xuser) + 1;
        this.guild = new UserStatsGuild(Main.getBot().getGuildById(xguild.getID()));

        User user = Main.getBot().getUserById(xuser.getID());
        if (user == null) {
            try { // user was not cached, fetch from discord
                user = Main.getBot().retrieveUserById(xuser.getID()).complete();
            } catch (Exception ignored) {}
        }

        if (user != null) {
            this.user = new APIUser(user);
        }

        chartEntries = new ArrayList<>();

        ResultSet rs = Database.executeQuery("SELECT * FROM xpStats WHERE userid = '" + xuser.getID() + "' AND guildid = '" + xguild.getID() + "' ORDER BY time DESC");

        if (rs != null) {
            try {
                while (rs.next()) {
                    chartEntries.add(new UserStatsChartEntry(rs.getInt("xp"), rs.getInt("rank"), rs.getLong("time")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class UserStatsChartEntry {
        public int level;
        public int xp;
        public int rank;
        public long time;

        public UserStatsChartEntry(int xp, int rank, long time) {
            this.level = XPUtils.calculateLevel(xp);
            this.xp = xp;
            this.rank = rank;
            this.time = time;
        }
    }

    private static class UserStatsGuild {
        public String name;
        public String icon;
        public String id;

        public UserStatsGuild(Guild guild) {
            if (guild == null) return;

            this.name = guild.getName();
            this.icon = guild.getIconUrl();
            this.id = guild.getId();
        }
    }
}
