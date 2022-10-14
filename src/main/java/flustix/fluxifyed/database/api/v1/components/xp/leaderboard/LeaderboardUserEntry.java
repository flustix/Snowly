package flustix.fluxifyed.database.api.v1.components.xp.leaderboard;

import flustix.fluxifyed.modules.xp.components.XPUser;
import net.dv8tion.jda.api.entities.User;

public class LeaderboardUserEntry {
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
