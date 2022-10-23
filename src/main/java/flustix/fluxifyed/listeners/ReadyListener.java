package flustix.fluxifyed.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.components.SlashCommandList;
import flustix.fluxifyed.modules.reactionroles.ReactionRoles;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.time.TimeUtils;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Main.LOGGER.info("Everything Ready! Took " + TimeUtils.format(System.currentTimeMillis() - Main.getStartTime()) + " to start");

        SlashCommandList.registerCommands(event);
        ReactionRoles.loadMessages();
    }

    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Main.LOGGER.info("Initializing guild '" + event.getGuild().getName() + "' (" + event.getGuild().getId() + ")");

        Settings.loadGuild(event.getGuild());

        for (Module module : Main.getModules())
            module.onGuildInit(event.getGuild());
    }
}
