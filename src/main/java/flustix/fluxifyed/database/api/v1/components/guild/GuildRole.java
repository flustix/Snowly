package flustix.fluxifyed.database.api.v1.components.guild;

import flustix.fluxifyed.database.api.v1.components.APIColor;
import net.dv8tion.jda.api.entities.Role;

public class GuildRole {
    public String id;
    public String name;
    public APIColor color;

    public GuildRole(Role role) {
        id = role.getId();
        name = role.getName();
        color = new APIColor(role.getColor());
    }
}
