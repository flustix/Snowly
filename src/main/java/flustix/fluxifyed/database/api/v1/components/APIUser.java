package flustix.fluxifyed.database.api.v1.components;

import net.dv8tion.jda.api.entities.User;

public class APIUser {
    public final String id;
    public final String name;
    public final String discriminator;
    public final String avatar;
    public final String banner;
    public final APIColor accentColor;

    public APIUser(User user) {
        id = user.getId();
        name = user.getName();
        discriminator = user.getDiscriminator();
        avatar = user.getAvatarUrl();

        User.Profile profile = user.retrieveProfile().complete();
        banner = profile.getBannerUrl();
        accentColor = new APIColor(profile.getAccentColor());
    }
}
