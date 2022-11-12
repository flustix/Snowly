package flustix.fluxifyed.database.api.v1.components.guild;

import net.dv8tion.jda.api.entities.Role;

public class GuildRole {
    public String id;
    public String name;
    public int color;
    public String colorString;

    public GuildRole(Role role) {
        this.id = role.getId();
        this.name = role.getName();

        if (role.getColor() != null) {
            this.color = role.getColorRaw();
            this.colorString = String.format("#%02x%02x%02x", role.getColor().getRed(), role.getColor().getGreen(), role.getColor().getBlue());
        } else {
            this.color = 0xffffff;
            this.colorString = "#ffffff";
        }
    }
}
