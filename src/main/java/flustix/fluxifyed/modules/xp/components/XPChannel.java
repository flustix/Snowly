package flustix.fluxifyed.modules.xp.components;

public class XPChannel {
    private final String id;
    private final int value;

    public XPChannel(String id, int level) {
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
