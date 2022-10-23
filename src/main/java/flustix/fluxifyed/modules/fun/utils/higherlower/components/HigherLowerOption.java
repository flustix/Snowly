package flustix.fluxifyed.modules.fun.utils.higherlower.components;

public class HigherLowerOption {
    private final String name;
    private final int value;

    public HigherLowerOption(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
