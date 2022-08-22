package flustix.fluxifyed.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommandList;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Main.LOGGER.info("Successfully logged in, shard " + event.getJDA().getShardInfo().getShardId());

        SlashCommandList.registerCommands(event);
    }
}
