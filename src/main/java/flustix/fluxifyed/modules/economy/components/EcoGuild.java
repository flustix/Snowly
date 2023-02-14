package flustix.fluxifyed.modules.economy.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;

import java.util.HashMap;

public class EcoGuild {
    private final String id;
    private final HashMap<String, EcoUser> users = new HashMap<>();

    public EcoGuild(String id) {
        this.id = id;
        loadUsers();
    }

    private void loadUsers() {
        Database.executeQuery("SELECT * FROM fluxifyed.balance WHERE guildid = '?'", new String[]{id}, resultSet -> {
            try {
                while (resultSet.next()) {
                    String userId = resultSet.getString("userid");
                    int balance = resultSet.getInt("balance");
                    int dailyStreak = resultSet.getInt("dailyStreak");
                    long lastDaily = resultSet.getLong("lastDaily");

                    EcoUser user = new EcoUser(id, userId, balance);
                    user.setDailyStreak(dailyStreak);
                    user.setLastDaily(lastDaily);
                    users.put(userId, user);
                }
            } catch (Exception e) {
                Main.LOGGER.error("Error while loading users from database", e);
            }
        });
    }

    public EcoUser getUser(String id) {
        EcoUser user = users.get(id);
        if (user == null) {
            user = new EcoUser(this.id, id, 0);
            users.put(id, user);
        }
        return user;
    }

    public String getId() {
        return id;
    }
}
