package flustix.fluxifyed.settings;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;

import java.sql.ResultSet;

public class UserSettings {
    String userId;

    boolean levelUpMessages = true;

    public UserSettings(String userId) {
        this.userId = userId;
        load();
    }

    void load() {
        Main.LOGGER.info("Loading user settings for user '" + userId + "'");

        ResultSet rs = Database.executeQuery("SELECT * FROM users WHERE userid = " + userId);
        boolean found = false;

        try {
            while (rs.next()) {
                found = true;
                levelUpMessages = rs.getBoolean("levelup");
            }
        } catch (Exception ignored) {
        }

        if (!found) {
            Database.executeQuery("INSERT INTO users (userid, levelup) VALUES ('" + userId + "', " + levelUpMessages + ")");
        }
    }

    void update() {
        Database.executeQuery("UPDATE users SET levelup = " + levelUpMessages + " WHERE userid = " + userId);
    }

    public String getUserId() {
        return userId;
    }

    public boolean levelUpMessagesEnabled() {
        return levelUpMessages;
    }

    public void setLevelUpMessagesEnabled(boolean enabled) {
        levelUpMessages = enabled;
        update();
    }
}
