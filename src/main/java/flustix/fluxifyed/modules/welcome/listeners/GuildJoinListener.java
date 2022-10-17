package flustix.fluxifyed.modules.welcome.listeners;

import flustix.fluxifyed.modules.welcome.WelcomeModule;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinListener extends ListenerAdapter {
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        WelcomeModule.sendWelcome(event);
    }
}
