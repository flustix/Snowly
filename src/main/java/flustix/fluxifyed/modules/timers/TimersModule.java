package flustix.fluxifyed.modules.timers;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.timers.components.TimedMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TimersModule extends Module {
    private static final List<TimedMessage> timedMessages = new ArrayList<>();

    public TimersModule() {
        super("timers", "Timers", "Send custom messages at a set interval.", true);
    }

    @Override
    public void init() {
        ResultSet rs = Database.executeQuery("SELECT * FROM timers");

        if (rs == null) return;

        try {
            while (rs.next()) {
                timedMessages.add(new TimedMessage(rs.getString("guildid"), rs.getString("channelid"), rs.getString("message"), rs.getString("time"), rs.getString("random")));
            }
        } catch (Exception ex) {
            Main.LOGGER.error("Error while loading timers!", ex);
        }

        new Thread(() -> {
            while (true) {
                try {
                    timedMessages.forEach(TimedMessage::send);
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        }).start();
    }
}
