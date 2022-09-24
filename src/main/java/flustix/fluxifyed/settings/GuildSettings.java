package flustix.fluxifyed.settings;

import flustix.fluxifyed.database.Database;

public class GuildSettings {
    private final String guildId;

    // modules
    private boolean xpModule = true;
    private boolean shopModule = false;

    public GuildSettings(String guildId) {
        this.guildId = guildId;
    }

    public void setup() {
        Database.executeQuery("INSERT INTO guilds (guildid, xpModule, shopModule) VALUES ('" + guildId + "', " + xpModule + ", " + shopModule + ")");
    }

    void update() {
        Database.executeQuery("UPDATE guilds SET xpModule = " + xpModule + ", shopModule = " + shopModule + " WHERE guildid = " + guildId);
    }

    public String getGuildId() {
        return guildId;
    }

    public boolean xpEnabled() {
        return xpModule;
    }

    public boolean shopEnabled() {
        return shopModule;
    }

    public void setXpEnabled(boolean enabled) {
        xpModule = enabled;
        update();
    }

    public void setShopEnabled(boolean enabled) {
        shopModule = enabled;
        update();
    }
}
