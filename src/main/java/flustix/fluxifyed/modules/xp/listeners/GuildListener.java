package flustix.fluxifyed.modules.xp.listeners;

import flustix.fluxifyed.modules.xp.XP;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        XP.initGuild(event.getGuild());
    }
}
