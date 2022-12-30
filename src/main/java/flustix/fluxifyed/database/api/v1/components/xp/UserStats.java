package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.settings.*;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.v1.components.APIMember;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserStats {
    public APIMember member;
    public long level;
    public long xp;
    public long xpLeft;
    public int rank;
    public UserStatsGuild guild;
    public List<UserStatsChartEntry> chartEntries;

    public UserStats(XPUser xuser, XPGuild xguild) {
    	GuildSettings settings = Settings.getGuildSettings(xguild.getID());
        this.level = XPUtils.calculateLevel(xuser.getXP(), settings.getString("xp.levelMode", "default"));
        this.xp = xuser.getXP();
        this.xpLeft = XPUtils.calculateXP(this.level + 1, settings.getString("xp.levelMode", "default")) - this.xp;
        this.rank = xguild.getTop().indexOf(xuser) + 1;
        this.guild = new UserStatsGuild(Main.getBot().getGuildById(xguild.getID()));

        Guild guild = Main.getBot().getGuildById(xguild.getID());
        if (guild != null) {
            Member member = guild.getMemberById(xuser.getID());
            if (member == null) {
                try { // user was not cached, fetch from discord
                    member = guild.retrieveMemberById(xuser.getID()).complete();
                } catch (Exception ignored) {}
            }
            if (member != null) {
                this.member = new APIMember(member);
            }
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
        public long level;
        public long xp;
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
