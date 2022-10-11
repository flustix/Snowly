package flustix.fluxifyed.modules.xp;

import flustix.fluxifyed.components.Module;
import net.dv8tion.jda.api.entities.Guild;

public class XPModule extends Module {
    public XPModule() {
        super("xp", "XP", "Reward your members for chatting and being active.");
    }

    public void onGuildInit(Guild guild) {
        XP.initGuild(guild);
    }
}
