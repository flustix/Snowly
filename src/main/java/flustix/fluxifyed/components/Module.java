package flustix.fluxifyed.components;

public class Module {
    public String name;
    public String description;
    public String id;

    public Module(String id, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * Called when the module is loaded.
     */
    public void init() {}
}
