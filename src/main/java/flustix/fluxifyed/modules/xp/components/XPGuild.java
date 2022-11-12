package flustix.fluxifyed.modules.xp.components;

import flustix.fluxifyed.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XPGuild {
    private final String id;
    private final HashMap<String, XPUser> users = new HashMap<>();
    private final List<XPRole> roles = new ArrayList<>();

    public XPGuild(String id) {
        this.id = id;
    }

    public XPUser getUser(String id) {
        XPUser user = users.get(id);

        if (user == null) {
            user = new XPUser(this.id, id);
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

    public void addRole(XPRole role) {
        roles.add(role);
    }

    /**
     * Removes all xp roles from the guild and adds the new ones
     */
    public void rebuildRoles(List<XPRole> roles) {
        this.roles.clear();
        Database.executeQuery("DELETE FROM xpRoles WHERE guildid = ?", id);

        this.roles.addAll(roles);

        for (XPRole role : roles) {
            Database.executeQuery("INSERT INTO xpRoles VALUES (?, ?, ?)", id, role.getID(), role.getLevel());
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

    public List<XPRole> getRoles() {
        return roles;
    }
}
