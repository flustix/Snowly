package flustix.fluxifyed.xp.types;

import flustix.fluxifyed.database.Database;

public class XPUser {
    private int xp = 0;
    private final String id;
    private final String gid;

    private long lastUpdate = 0;

    public XPUser(String gid, String id) {
        this.id = id;
        this.gid = gid;
    }

    public void addXP(int xp) {
        if (lastUpdate + 60000 > System.currentTimeMillis())
            return;

        this.xp += xp;
        updateXP();
        lastUpdate = System.currentTimeMillis();
    }

    public void giveXP(int xp) {
        this.xp += xp;
        updateXP();
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public void updateXP() {
        Database.executeQuery("INSERT INTO xp (guildid, userid, xp) VALUES ('" + gid + "', '" + id + "', " + xp + ") ON DUPLICATE KEY UPDATE xp = " + this.xp);
    }

    public int getXP() {
        return xp;
    }

    public String getID() {
        return id;
    }

    public String getGuildID() {
        return gid;
    }
}
