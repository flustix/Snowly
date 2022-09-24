package flustix.fluxifyed.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommandList;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.xp.XP;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        long ms = System.currentTimeMillis() - Main.getStartTime();
        long s = ms / 1000;
        Main.LOGGER.info("Everything Ready! Took {} second(s) and {}ms to start", s, ms - s * 1000);

        SlashCommandList.registerCommands(event);
    }

    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Main.LOGGER.info("Initializing guild '" + event.getGuild().getName() + "' (" + event.getGuild().getId() + ")");

        Settings.loadGuild(event.getGuild());
        XP.initGuild(event.getGuild());
    }
}
