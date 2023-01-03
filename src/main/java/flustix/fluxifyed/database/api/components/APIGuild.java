package flustix.fluxifyed.database.api.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.TreeMap;

public class APIGuild {
    public final String id;
    public final String name;
    public final String owner;
    public final String icon;
    public final String banner;
    public final String splash;

    public final TreeMap<String, Boolean> modules = new TreeMap<>();

    public APIGuild(Guild guild) {
        id = guild.getId();
        name = guild.getName();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();
        splash = guild.getSplashUrl();

        if (guild.getOwner() != null) {
            owner = guild.getOwner().getUser().getAsTag();
        } else {
            Main.LOGGER.warn("Guild Member intent is disabled!");
            owner = "0";
        }

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        for (Module module : Main.getModules()) {
            modules.put(module.id, settings.getBoolean(module.id + ".enabled", true));
        }
    }

    public APIGuild(String id) {
        this.id = id;
        name = "Unknown";
        owner = "0";
        icon = "";
        banner = "";
        splash = "";
    }
}
