package flustix.fluxifyed.database.api.components.xp;

import flustix.fluxifyed.database.api.components.APIColor;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RewardRoles {
    public List<RewardRole> roles;

    public RewardRoles(Guild guild) {
        XPGuild xpGuild = XP.getGuild(guild.getId());
        GuildSettings settings = Settings.getGuildSettings(guild.getId());

        roles = new ArrayList<>();

        for (XPRole role : xpGuild.getLevelRoles()) {
            Role guildRole = guild.getRoleById(role.getID());

            roles.add(new RewardRole(settings, guildRole, role));
        }

        roles.sort(Comparator.comparingLong(a -> a.level));
    }

    private static class RewardRole {
        public String id;
        public String name;
        public long level;
        public long xp;
        public APIColor color;

        public RewardRole(GuildSettings settings, Role role, XPRole xpRole) {
            String levelMode = settings.getString("xp.levelMode", "default");

            if (role == null) {
                id = xpRole.getID();
                name = "Unknown";
                color = new APIColor(null);
            } else {
                id = role.getId();
                name = role.getName();
                color = new APIColor(role.getColor());
            }

            if (xpRole == null) {
                level = 0;
                xp = 0;
            } else {
                level = xpRole.getValue();
                xp = XPUtils.calculateXP(level, levelMode);
            }
        }
    }
}
