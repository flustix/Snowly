package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.Guild;

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

    private static class GlobalLeaderboardEntry {
        final String id;
        final String name;
        final String icon;
        final int xp;

        public GlobalLeaderboardEntry(String gid, int xp) {
            this.id = gid;
            this.xp = xp;

            Guild guild = Main.getBot().getGuildById(gid);

            if (guild == null) {
                this.name = "Unknown";
                this.icon = "https://cdn.discordapp.com/embed/avatars/0.png";
            } else {
                this.name = guild.getName();
                this.icon = guild.getIconUrl();
            }
        }

        public int getXP() {
            return xp;
        }
    }
}
