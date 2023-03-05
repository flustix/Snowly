package flustix.fluxifyed.modules.xp.components;

public class XPChannel {
    private final String id;
    private final float value;

    public XPChannel(String id, float level) {
        this.id = id;
        this.value = level;
    }

    public String getID() {
        return id;
    }

    public float getValue() {
        return value;
    }
}
