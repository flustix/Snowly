package flustix.fluxifyed.modules.economy.components;

import flustix.fluxifyed.database.Database;

import java.util.concurrent.TimeUnit;

public class EcoUser {
    private final String guildId;
    private final String id;
    private int balance;
    private int dailyStreak = 0;
    private long lastDaily = 0;

    public EcoUser(String guildId, String id, int balance) {
        this.guildId = guildId;
        this.id = id;
        this.balance = balance;
    }

    private void save() {
        Database.executeQuery("INSERT INTO fluxifyed.balance (userid, guildid, balance, dailyStreak, lastDaily) VALUES ('?', '?', ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?, dailyStreak = ?, lastDaily = ?",
                id, guildId, String.valueOf(balance), String.valueOf(dailyStreak), String.valueOf(lastDaily), String.valueOf(balance), String.valueOf(dailyStreak), String.valueOf(lastDaily));
    }

    public boolean canDaily() {
        long delta = System.currentTimeMillis() - lastDaily;
        return delta > TimeUnit.HOURS.toMillis(20);
    }

    public int claimDaily() {
        if (overDailyStreak())
            resetDailyStreak();

        int reward = 100 + (dailyStreak * 100);

        addBalance(reward);
        addDailyStreak();
        updateDaily();
        save();

        return reward;
    }

    public void updateDaily() {
        lastDaily = System.currentTimeMillis();
    }

    public void setLastDaily(long lastDaily) {
        this.lastDaily = lastDaily;
    }

    public void addDailyStreak() {
        dailyStreak++;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public boolean overDailyStreak() {
        return System.currentTimeMillis() - lastDaily >= TimeUnit.HOURS.toMillis(40);
    }

    public void resetDailyStreak() {
        dailyStreak = 0;
    }

    public int getDailyStreak() {
        return dailyStreak;
    }

    public long getLastDaily() {
        return lastDaily;
    }

    public long nextDaily() {
        return lastDaily + TimeUnit.HOURS.toMillis(20);
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void removeBalance(int amount) {
        this.balance -= amount;
    }

    public boolean hasBalance(int amount) {
        return this.balance >= amount;
    }
}
