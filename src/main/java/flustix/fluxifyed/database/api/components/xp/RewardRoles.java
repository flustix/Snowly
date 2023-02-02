package flustix.fluxifyed.database.api.components.xp;

import flustix.fluxifyed.database.api.components.APIColor;
import flustix.fluxifyed.database.api.components.guild.GuildRole;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RewardRoles {
    public List<RewardRole> roles;

    public RewardRoles(@NotNull Guild guild) {
        XPGuild xpGuild = XP.getGuild(guild.getId());
        GuildSettings settings = Settings.getGuildSettings(guild.getId());

        roles = new ArrayList<>();

        for (XPRole role : xpGuild.getLevelRoles()) {
            Role guildRole = guild.getRoleById(role.getID());

            roles.add(new RewardRole(settings, guildRole, role));
        }

        roles.sort(Comparator.comparingLong(a -> a.level));
    }

    private static class RewardRole extends GuildRole {
        public String id;
        public String name;
        public long level;
        public long xp;
        public APIColor color;

        public RewardRole(@NotNull GuildSettings settings, @Nullable Role role, @NotNull XPRole xpRole) {
            super(role);

            // If the role is null, we can set the id since we know it thru the XPRole
            if (role == null) id = xpRole.getID();

            String levelMode = settings.getString("xp.levelMode", "default");
            level = xpRole.getValue();
            xp = XPUtils.calculateXP(level, levelMode);
        }
    }
}
