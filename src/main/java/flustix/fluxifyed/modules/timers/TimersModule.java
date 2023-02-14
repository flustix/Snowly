package flustix.fluxifyed.modules.timers;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.timers.components.TimedMessage;

import java.sql.ResultSet;
import java.util.HashMap;

public class TimersModule extends Module {
    private static final HashMap<Integer, TimedMessage> timedMessages = new HashMap<>();

    public TimersModule() {
        super("timers", "Timers", "Send custom messages at a set interval.");
    }

    @Override
    public void init() {
        ResultSet rs = Database.executeQuery("SELECT * FROM fluxifyed.timers");

        if (rs == null) return;

        try {
            while (rs.next()) {
                timedMessages.put(rs.getInt("id"), new TimedMessage(rs.getString("guildid"), rs.getString("channelid"), rs.getString("message"), rs.getString("time"), rs.getString("random")));
            }
        } catch (Exception ex) {
            Main.LOGGER.error("Error while loading timers!", ex);
        }

        new Thread(() -> {
            while (true) {
                try {
                    timedMessages.forEach((id, message) -> message.send());
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        }).start();
    }

    public static TimedMessage getTimer(int id) {
        return timedMessages.get(id);
    }
}
