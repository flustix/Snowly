package flustix.fluxifyed.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GuildSettings {
    private final String guildId;
    private final HashMap<String, JsonElement> settings = new HashMap<>();

    public GuildSettings(String guildId) {
        this.guildId = guildId;
        load();
    }

    private void load() {
        ResultSet rs = Database.executeQuery("SELECT * FROM guilds WHERE guildid = '" + guildId + "'");

        if (rs == null) {
            Main.LOGGER.info("Failed to load guild settings for guild " + guildId);
            return;
        }

        try {
            while (rs.next()) {
                String data = rs.getString("data");
                JsonObject json = JsonParser.parseString(data).getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    if (entry.getValue().isJsonObject()) {
                        JsonObject catJson = entry.getValue().getAsJsonObject();

                        for (Map.Entry<String, JsonElement> catEntry : catJson.entrySet()) {
                            settings.put(entry.getKey() + "." + catEntry.getKey(), catEntry.getValue());
                        }
                    } else {
                        settings.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            Main.LOGGER.info("Failed to load guild settings for guild " + guildId);
            e.printStackTrace();
        }
    }

    public void save() {
        JsonObject json = new JsonObject();

        for (Map.Entry<String, JsonElement> entry : settings.entrySet()) {
            String[] split = entry.getKey().split("\\.");

            if (split.length == 1) {
                json.add(split[0], entry.getValue());
            } else {
                if (!json.has(split[0])) {
                    json.add(split[0], new JsonObject());
                }

                json.getAsJsonObject(split[0]).add(split[1], entry.getValue());
            }
        }

        String escaped = Database.escape(json.toString());

        Database.executeQuery("INSERT INTO guilds (guildid, data) VALUES ('" + guildId + "', '" + escaped + "') ON DUPLICATE KEY UPDATE data = '" + escaped + "'");
    }

    public JsonElement get(String setting) {
        return settings.getOrDefault(setting, null);
    }

    public void set(String setting, Object value) {
        JsonElement element;

        if (value instanceof String) {
            element = new JsonPrimitive((String) value);
        } else if (value instanceof Boolean) {
            element = new JsonPrimitive((Boolean) value);
        } else if (value instanceof Number) {
            element = new JsonPrimitive((Number) value);
        } else {
            return;
        }

        settings.put(setting, element);
        save();
    }

    public boolean getBoolean(String setting, boolean defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsBoolean();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getString(String setting, String defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(String setting, int defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsInt();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String setting, long defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsLong();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String setting, double defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsDouble();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(String setting, float defaultValue) {
        JsonElement element = get(setting);
        if (element == null) return defaultValue;

        try {
            return element.getAsFloat();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
