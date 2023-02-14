package flustix.fluxifyed.modules.economy;

import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.modules.economy.components.*;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class EconomyModule extends Module {
    private static final HashMap<String, EcoGuild> guilds = new HashMap<>();

    public EconomyModule() {
        super("economy", "Economy", "economy module desc");
    }

    public void onGuildInit(Guild guild) {
        guilds.put(guild.getId(), new EcoGuild(guild.getId()));
    }

    public static EcoGuild getGuild(String guildId) {
        return guilds.getOrDefault(guildId, new EcoGuild(guildId));
    }
}
