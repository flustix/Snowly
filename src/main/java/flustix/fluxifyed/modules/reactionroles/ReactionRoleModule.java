package flustix.fluxifyed.modules.reactionroles;

import flustix.fluxifyed.components.Module;
import net.dv8tion.jda.api.entities.Guild;

public class ReactionRoleModule extends Module {
    public ReactionRoleModule() {
        super("reactionroles", "Reaction Roles", "Easily create reaction roles for your members to get roles.");
    }

    public void onGuildInit(Guild guild) {
        ReactRoles.loadGuild(guild);
    }
}
