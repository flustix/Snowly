package flustix.fluxifyed.database.api.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.settings.*;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.components.APIMember;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
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
    public long xpNeededForLevel;
    public int rank;
    public UserStatsGuild guild;
    public List<UserStatsChartEntry> chartEntries;

    public UserStats(XPUser xpUser, XPGuild xpGuild) {
    	GuildSettings settings = Settings.getGuildSettings(xpGuild.getID());
        String levelMode = settings.getString("xp.levelMode", "default");

        this.level = XPUtils.calculateLevel(xpUser.getXP(), levelMode);
        this.xp = xpUser.getXP();
        this.xpLeft = XPUtils.calculateXP(this.level + 1, levelMode) - this.xp;
        this.xpNeededForLevel = XPUtils.calculateXP(this.level, levelMode);
        this.rank = xpGuild.getTop().indexOf(xpUser) + 1;
        this.guild = new UserStatsGuild(Main.getBot().getGuildById(xpGuild.getID()));

        Guild guild = Main.getBot().getGuildById(xpGuild.getID());
        if (guild != null) {
            Member member = guild.getMemberById(xpUser.getID());
            if (member == null) {
                try { // user was not cached, fetch from discord
                    member = guild.retrieveMemberById(xpUser.getID()).complete();
                } catch (Exception ignored) {}
            }
            if (member != null) {
                this.member = new APIMember(member);
            }
        }

        if (member == null) {
            member = new APIMember(xpUser.getID());
        }

        chartEntries = new ArrayList<>();

        ResultSet rs = Database.executeQuery("SELECT * FROM xpStats WHERE userid = '" + xpUser.getID() + "' AND guildid = '" + xpGuild.getID() + "' ORDER BY time DESC");

        if (rs != null) {
            try {
                while (rs.next()) {
                    chartEntries.add(new UserStatsChartEntry(rs.getInt("xp"), rs.getInt("rank"), rs.getLong("time"), levelMode));
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

        public UserStatsChartEntry(int xp, int rank, long time, String levelMode) {
            this.level = XPUtils.calculateLevel(xp, levelMode);
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
