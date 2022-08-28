package flustix.fluxifyed.xp.types;

import flustix.fluxifyed.Main;
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
        Database.executeQuery("INSERT INTO xp (guildid, userid, xp) VALUES ('" + gid + "', '" + id + "', " + xp + ") ON DUPLICATE KEY UPDATE xp = " + this.xp);
        lastUpdate = System.currentTimeMillis();
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
