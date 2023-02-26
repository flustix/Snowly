package flustix.fluxifyed.modules.moderation.automod;

import flustix.fluxifyed.modules.moderation.automod.components.AutoModGuild;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;

public class AutoMod {
    private static final HashMap<String, AutoModGuild> guilds = new HashMap<>();

    public static void loadGuild(Guild guild) {
        guilds.put(guild.getId(), new AutoModGuild(guild.getId()));
    }

    public static void checkMessage(Message message) {
        AutoModGuild guild = guilds.get(message.getGuild().getId());
        if (guild == null) return;

        guild.checkMessage(message);
    }
}
