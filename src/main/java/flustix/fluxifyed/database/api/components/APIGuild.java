package flustix.fluxifyed.database.api.components;

import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

public class APIGuild {
    public String id;
    public String name;
    public String owner;
    public String icon;
    public String banner;

    public boolean xpEnabled;
    public boolean shopEnabled;

    public APIGuild(Guild guild) {
        id = guild.getId();
        name = guild.getName();
        owner = guild.getOwner().getUser().getId();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        xpEnabled = settings.xpEnabled();
        shopEnabled = settings.shopEnabled();
    }
}
