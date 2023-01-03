package flustix.fluxifyed.database.api.v1.components;

import flustix.fluxifyed.utils.AvatarUtils;
import net.dv8tion.jda.api.entities.User;

public class APIUser {
    public final String id;
    public final String name;
    public final String discriminator;
    public String avatar;
    public String banner;
    public APIColor accentColor;

    public APIUser(User user) {
        id = user.getId();
        name = user.getName();
        discriminator = user.getDiscriminator();
        avatar = user.getAvatarUrl();

        User.Profile profile = user.retrieveProfile().complete();
        banner = profile.getBannerUrl();
        accentColor = new APIColor(profile.getAccentColor());
    }

    public APIUser(String id) {
        this.id = id;
        name = "Unknown";
        discriminator = "0000";
        avatar = AvatarUtils.getDefaultAvatar();
        banner = "";
        accentColor = new APIColor(null);
    }
}
