package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.settings.*;
import flustix.fluxifyed.utils.xp.XPUtils;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class GuildLeaderboard {
    public final String name;
    public final String icon;
    public final String banner;
    public final List<LeaderboardUserEntry> entries = new ArrayList<>();

    public GuildLeaderboard(Guild guild, int limit, int offset) {
        name = guild.getName();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();

        XPGuild xpGuild = XP.getGuild(guild.getId());
        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        String levelMode = settings.getString("xp.levelMode", "default");

        int i = 0;

        for (XPUser xpUser : xpGuild.getTop()) {
            if (i == limit + offset) break;

            if (i >= offset) {
                User user = guild.getJDA().getUserById(xpUser.getID());

                try {
                    if (user == null) user = Main.getBot().retrieveUserById(xpUser.getID()).complete();
                    entries.add(new LeaderboardUserEntry(user, xpUser, levelMode));
                } catch (NullPointerException e) {
                    entries.add(new LeaderboardUserEntry(xpUser, levelMode));
                }
            }

            i++;
        }
    }

    private static class LeaderboardUserEntry {
        public final String username;
        public final String discriminator;
        public final String avatar;
        public final long xp;
        public final long level;
        public final long xpLeft;

        public LeaderboardUserEntry(User user, XPUser xpUser, String levelMode) {
            username = user.getName();
            discriminator = user.getDiscriminator();
            avatar = user.getEffectiveAvatarUrl();
            xp = xpUser.getXP();
            level = XPUtils.calculateLevel(xp, levelMode);
            xpLeft = XPUtils.calculateXP(level + 1, levelMode) - xp;
        }

        public LeaderboardUserEntry(XPUser xpUser, String levelMode) {
            username = "Unknown";
            discriminator = "0000";
            avatar = "https://cdn.discordapp.com/embed/avatars/0.png";
            xp = xpUser.getXP();
            level = XPUtils.calculateLevel(xp, levelMode);
            xpLeft = XPUtils.calculateXP(level + 1, levelMode) - xp;
        }
    }
}
