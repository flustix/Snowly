package flustix.fluxifyed.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.xp.XP;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Main.LOGGER.info("Added to guild '" + event.getGuild().getName() + "' (" + event.getGuild().getId() + ")");
        XP.initGuild(event.getGuild());
        Settings.loadGuild(event.getGuild());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        Main.LOGGER.info("Removed from guild '" + event.getGuild().getName() + "' (" + event.getGuild().getId() + ")");
    }
}
