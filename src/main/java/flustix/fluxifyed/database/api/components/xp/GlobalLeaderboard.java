package flustix.fluxifyed.database.api.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.components.APIGuild;
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

                    entries.add(new GlobalLeaderboardEntry(Main.getBot().getGuildById(guildId), xp));
                }
            }

            i.getAndIncrement();
        });

        entries.sort((a, b) -> b.getXP() - a.getXP());
    }

    private static class GlobalLeaderboardEntry {
        public final APIGuild guild;
        public final int xp;

        public GlobalLeaderboardEntry(Guild guild, int xp) {
            this.guild = new APIGuild(guild);
            this.xp = xp;
        }

        public int getXP() {
            return xp;
        }
    }
}
