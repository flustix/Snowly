package flustix.fluxifyed.components;

import net.dv8tion.jda.api.entities.Guild;

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

    /**
     * Called in the guild initialization.
     */
    public void onGuildInit(Guild guild) {}
}
