package flustix.fluxifyed.modules.xp;

import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.modules.xp.utils.StatTask;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.TimeUnit;

public class XPModule extends Module {
    public XPModule() {
        super("xp", "XP", "Reward your members for chatting and being active.");
    }

    public void init() {
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1)); // check every minute if it's 00:00
                    StatTask.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onGuildInit(Guild guild) {
        XP.initGuild(guild);
    }
}
