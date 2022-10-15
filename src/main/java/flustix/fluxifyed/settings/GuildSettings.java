package flustix.fluxifyed.settings;

import flustix.fluxifyed.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildSettings {
    private final String guildId;

    private final HashMap<String, Boolean> modules = new HashMap<>();

    public GuildSettings(String guildId) {
        this.guildId = guildId;
    }

    public void setup() {
        Database.executeQuery("INSERT INTO guilds (guildid) VALUES ('" + guildId + "')");
    }

    void update() {
        List<String> moduleQueries = new ArrayList<>();

        for (String module : modules.keySet())
            moduleQueries.add(module + "Module = " + modules.get(module));

        Database.executeQuery("UPDATE guilds SET " + String.join(", ", moduleQueries) + " WHERE guildid = " + guildId);
    }

    public String getGuildId() {
        return guildId;
    }

    public boolean moduleEnabled(String module) {
        return modules.getOrDefault(module, false);
    }

    public void setModuleEnabled(String module, boolean enabled) {
        modules.put(module, enabled);
        update();
    }
}
