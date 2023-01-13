package flustix.fluxifyed.database.api.components.xp;

import flustix.fluxifyed.database.api.components.APIGuild;
import flustix.fluxifyed.database.api.components.APIMember;
import flustix.fluxifyed.settings.*;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class GuildLeaderboard {
    public APIGuild guild;
    public final List<LeaderboardUserEntry> entries = new ArrayList<>();

    public GuildLeaderboard(Guild guild, int limit, int offset) {
        this.guild = new APIGuild(guild);

        XPGuild xpGuild = XP.getGuild(guild.getId());
        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        String levelMode = settings.getString("xp.levelMode", "default");

        int i = 0;

        for (XPUser xpUser : xpGuild.getTop()) {
            if (i == limit + offset) break;

            if (i >= offset) {
                Member member = guild.getMemberById(xpUser.getID());

                try {
                    if (member == null) member = guild.retrieveMemberById(xpUser.getID()).complete();
                    entries.add(new LeaderboardUserEntry(member, xpUser, levelMode));
                } catch (Exception e) {
                    entries.add(new LeaderboardUserEntry(xpUser, levelMode));
                }
            }

            i++;
        }
    }

    private static class LeaderboardUserEntry {
        public final APIMember member;
        public final long xp;
        public final long level;
        public final long xpNeededForLevel;
        public final long xpLeft;

        public LeaderboardUserEntry(Member guildMember, XPUser xpUser, String levelMode) {
            member = new APIMember(guildMember);
            xp = xpUser.getXP();
            level = XPUtils.calculateLevel(xp, levelMode);
            xpLeft = XPUtils.calculateXP(level + 1, levelMode) - xp;
            xpNeededForLevel = XPUtils.calculateXP(level, levelMode);
        }

        public LeaderboardUserEntry(XPUser xpUser, String levelMode) {
            member = new APIMember(xpUser.getID());
            xp = xpUser.getXP();
            level = XPUtils.calculateLevel(xp, levelMode);
            xpLeft = XPUtils.calculateXP(level + 1, levelMode) - xp;
            xpNeededForLevel = XPUtils.calculateXP(level, levelMode);
        }
    }
}
