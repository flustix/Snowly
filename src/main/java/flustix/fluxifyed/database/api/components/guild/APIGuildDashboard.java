package flustix.fluxifyed.database.api.components.guild;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.api.components.APIGuild;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.TreeMap;

public class APIGuildDashboard extends APIGuild {
    public final TreeMap<String, Boolean> modules = new TreeMap<>();

    public APIGuildDashboard(Guild guild) {
        super(guild);

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        for (Module module : Main.getModules()) {
            modules.put(module.id, settings.getBoolean(module.id + ".enabled", true));
        }
    }

    public APIGuildDashboard(String id) {
        super(id);
    }
}
