package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPUser;

import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderboard {
    public final List<GlobalLeaderboardEntry> entries;

    public GlobalLeaderboard() {
        entries = new ArrayList<>();

        XP.getGuilds().forEach((guildId, guild) -> {
            int xp = 0;

            for (XPUser user : guild.getTop()) {
                xp += user.getXP();
            }

            entries.add(new GlobalLeaderboardEntry(guildId, xp));
        });

        entries.sort((a, b) -> b.getXP() - a.getXP());
    }

    static class GlobalLeaderboardEntry {
        final String guildid;
        final int xp;

        public GlobalLeaderboardEntry(String gid, int xp) {
            this.guildid = gid;
            this.xp = xp;
        }

        public int getXP() {
            return xp;
        }
    }
}
