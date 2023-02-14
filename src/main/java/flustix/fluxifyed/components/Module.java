package flustix.fluxifyed.components;

import net.dv8tion.jda.api.entities.Guild;

public class Module {
    public final String name;
    public final String description;
    public final String id;

    public Module(String id, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * Called when the module is loaded.
     */
    public void init() {
        // Override this method to do something when the module is loaded.
    }

    /**
     * Called in the guild initialization.
     */
    public void onGuildInit(Guild guild) {
        // Override this method to do something when the module is loaded.
    }
}
