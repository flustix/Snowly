package flustix.fluxifyed.database.api.components.guild;

import flustix.fluxifyed.database.api.components.APIColor;
import net.dv8tion.jda.api.entities.Role;

public class GuildRole {
    public String id;
    public String name;
    public APIColor color;

    public GuildRole(Role role) {
        if (role == null) {
            id = "0";
            name = "Unknown";
            color = new APIColor(null);
        } else {
            id = role.getId();
            name = role.getName();
            color = new APIColor(role.getColor());
        }
    }

    public GuildRole(String id) {
        this.id = id;
        this.name = "Unknown";
        this.color = new APIColor(null);
    }
}
