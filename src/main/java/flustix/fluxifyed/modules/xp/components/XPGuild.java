package flustix.fluxifyed.modules.xp.components;

import flustix.fluxifyed.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XPGuild {
    private final String id;
    private final HashMap<String, XPUser> users = new HashMap<>();
    private final List<XPRole> levelRoles = new ArrayList<>();
    private final List<XPRole> multipliers = new ArrayList<>();

    public XPGuild(String id) {
        this.id = id;
    }

    public XPUser getUser(String id) {
        XPUser user = users.get(id);

        if (user == null) {
            user = new XPUser(this, id);
            users.put(id, user);
        }

        return user;
    }

    public boolean hasUser(String id) {
        return users.containsKey(id);
    }

    public void addUser(XPUser user) {
        users.put(user.getID(), user);
    }

    public void addLevelRole(XPRole role) {
        levelRoles.add(role);
    }

    public void addMultiplier(XPRole role) {
        multipliers.add(role);
    }

    /**
     * Removes all xp roles from the guild and adds the new ones
     */
    public void rebuildLevelRoles(List<XPRole> newRoles) {
        levelRoles.clear();
        Database.executeQuery("DELETE FROM xpRoles WHERE guildid = ? AND type = 'level'", id);

        levelRoles.addAll(newRoles);

        for (XPRole role : newRoles) {
            Database.executeQuery("INSERT INTO xpRoles (guildid, roleid, value, type) VALUES (?, ?, ?, 'level')", id, role.getID(), role.getValue());
        }
    }

    public List<XPUser> getTop() {
        List<XPUser> top = new ArrayList<>(users.values().stream().toList());
        top.sort((a, b) -> b.getXP() - a.getXP());
        return top;
    }

    public String getID() {
        return id;
    }

    public List<XPRole> getLevelRoles() {
        return levelRoles;
    }

    public List<XPRole> getMultipliers() {
        return multipliers;
    }
}
