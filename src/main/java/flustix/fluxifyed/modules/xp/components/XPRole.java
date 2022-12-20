package flustix.fluxifyed.modules.xp.components;

/**
 * Used for both leveling up and multipliers
 */
public class XPRole {
    private final String id;
    private final int value;

    public XPRole(String id, int level) {
        this.id = id;
        this.value = level;
    }

    public String getID() {
        return id;
    }

    public int getValue() {
        return value;
    }
}
