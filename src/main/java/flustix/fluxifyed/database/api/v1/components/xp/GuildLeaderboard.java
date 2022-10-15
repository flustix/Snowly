package flustix.fluxifyed.database.api.v1.components.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class GuildLeaderboard {
    public String name;
    public String icon;
    public String banner;
    public List<LeaderboardUserEntry> entries = new ArrayList<>();

    public GuildLeaderboard(Guild guild) {
        name = guild.getName();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();

        XPGuild xpGuild = XP.getGuild(guild.getId());

        for (XPUser xpUser : xpGuild.getTop()) {
            User user = guild.getJDA().getUserById(xpUser.getID());

            try {
                if (user == null) user = Main.getBot().retrieveUserById(xpUser.getID()).complete();
                entries.add(new LeaderboardUserEntry(user, xpUser));
            } catch (NullPointerException e) {
                entries.add(new LeaderboardUserEntry(xpUser));
            }

        }
    }

    private static class LeaderboardUserEntry {
        public String username;
        public String discriminator;
        public String avatar;
        public int xp;

        public LeaderboardUserEntry(User user, XPUser xpUser) {
            username = user.getName();
            discriminator = user.getDiscriminator();
            avatar = user.getAvatarUrl();
            this.xp = xpUser.getXP();
        }

        public LeaderboardUserEntry(XPUser xpUser) {
            this.username = "Unknown";
            this.discriminator = "0000";
            this.avatar = "https://cdn.discordapp.com/embed/avatars/0.png";
            this.xp = xpUser.getXP();
        }
    }
}
