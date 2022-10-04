package flustix.fluxifyed.database.api.v1.components;

import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Objects;

public class APIGuild {
    public final String id;
    public final String name;
    public final String owner;
    public final String icon;
    public final String banner;
    public final String splash;

    public final boolean xpEnabled;
    public final boolean shopEnabled;

    public APIGuild(Guild guild) {
        id = guild.getId();
        name = guild.getName();
        owner = Objects.requireNonNull(guild.getOwner()).getUser().getId();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();
        splash = guild.getSplashUrl();

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        xpEnabled = settings.xpEnabled();
        shopEnabled = settings.shopEnabled();
    }
}
