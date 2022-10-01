package flustix.fluxifyed.database.api.v1.components;

import net.dv8tion.jda.api.entities.User;

public class APIUser {
    public String id;
    public String name;
    public String discriminator;
    public String avatar;
    public String banner;

    public APIUser(User user) {
        id = user.getId();
        name = user.getName();
        discriminator = user.getDiscriminator();
        avatar = user.getAvatarUrl();

        User.Profile profile = user.retrieveProfile().complete();
        banner = profile.getBannerUrl();
    }
}
