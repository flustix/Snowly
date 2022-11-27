package flustix.fluxifyed.activity;

import flustix.fluxifyed.Main;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.TimeUnit;

public class ActivityUpdater {
    public static void start() {
        new Thread(() -> {
            while (true) {
                try {
                    update();
                    Thread.sleep(TimeUnit.HOURS.toMillis(1)); // update every hour
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void update() {
        int guildCount = 0;
        int memberCount = 0;

        for (var guild : Main.getBot().getGuilds()) {
            guildCount++;
            memberCount += guild.getMemberCount();
        }

        Main.getBot().getPresence().setActivity(Activity.watching(guildCount + " guilds with " + memberCount + " members"));
    }
}
