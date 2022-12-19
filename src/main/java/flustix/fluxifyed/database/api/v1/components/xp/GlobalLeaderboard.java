package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalLeaderboard {
    public final List<GlobalLeaderboardEntry> entries;

    public GlobalLeaderboard(int limit, int offset) {
        entries = new ArrayList<>();

        AtomicInteger i = new AtomicInteger();

        XP.getGuilds().forEach((guildId, guild) -> {
            if (i.get() == limit) return;

            if (i.get() >= offset) {
                GuildSettings settings = Settings.getGuildSettings(guildId);

                if (settings.getBoolean("xp.enabled", true)) {
                    int xp = 0;

                    for (XPUser user : guild.getTop()) {
                        xp += user.getXP();
                    }

                    entries.add(new GlobalLeaderboardEntry(guildId, xp));
                }
            }

            i.getAndIncrement();
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
