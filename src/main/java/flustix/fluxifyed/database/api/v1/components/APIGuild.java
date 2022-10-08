package flustix.fluxifyed.database.api.v1.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Objects;
import java.util.TreeMap;

public class APIGuild {
    public final String id;
    public final String name;
    public final String owner;
    public final String icon;
    public final String banner;
    public final String splash;

    public TreeMap<String, Boolean> modules = new TreeMap<>();

    public APIGuild(Guild guild) {
        id = guild.getId();
        name = guild.getName();
        owner = Objects.requireNonNull(guild.getOwner()).getUser().getId();
        icon = guild.getIconUrl();
        banner = guild.getBannerUrl();
        splash = guild.getSplashUrl();

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        for (Module module : Main.getModules()) {
            modules.put(module.id, settings.moduleEnabled(module.id));
        }
    }
}
