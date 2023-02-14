package flustix.fluxifyed.modules.welcome;

import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.modules.welcome.components.WelcomeData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class WelcomeModule extends Module {
    private static final Map<String, WelcomeData> welcomeDataMap = new HashMap<>();

    public WelcomeModule() {
        super("welcome", "Welcome", "Customizable welcome messages for new members.");
    }

    @Override
    public void onGuildInit(Guild guild) {
        welcomeDataMap.put(guild.getId(), new WelcomeData(guild.getId()));
    }

    public static void sendWelcome(GuildMemberJoinEvent event) {
        if (welcomeDataMap.containsKey(event.getGuild().getId())) {
            welcomeDataMap.get(event.getGuild().getId()).sendMessage(event);
        }
    }
}
