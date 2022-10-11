package flustix.fluxifyed.modules.xp.components;

public class XPRole {
    private final String id;
    private final int level;

    public XPRole(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public String getID() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
