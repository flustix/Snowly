package flustix.fluxifyed.database.api.components;

import net.dv8tion.jda.api.entities.Guild;

public class APIGuild {
    public final String id;
    public final String name;
    public final String owner;
    public final String icon;
    public final String banner;
    public final String splash;
    public final int memberCount;

    public APIGuild(Guild guild) {
        if (guild == null) {
            id = "";
            name = "Unknown";
            owner = "0";
            icon = "";
            banner = "";
            splash = "";
            memberCount = 0;
            return;
        }

        id = guild.getId();
        name = guild.getName();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();
        splash = guild.getSplashUrl();
        owner = guild.getOwnerId();
        memberCount = guild.getMemberCount();
    }

    public APIGuild(String id) {
        this.id = id;
        name = "Unknown";
        owner = "0";
        icon = "";
        banner = "";
        splash = "";
        memberCount = 0;
    }
}
