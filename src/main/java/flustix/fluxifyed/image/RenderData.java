package flustix.fluxifyed.image;

import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.color.ColorUtils;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.HashMap;

public class RenderData {
    final Guild guild;
    final Member member;

    final HashMap<String, String> data = new HashMap<>();
    public boolean loaded = false;

    public RenderData(Guild guild, Member member) {
        this.guild = guild;
        this.member = member;

        initKeys();
    }

    void initKeys() {
        // guild.-
        data.put("guild.name", guild.getName());
        data.put("guild.icon", guild.getIconUrl());
        String splash = guild.getSplashUrl();
        data.put("guild.splash", splash == null ? "" : splash);

        // member.-
        data.put("member.nickname", member.getEffectiveName());
        data.put("member.name", member.getUser().getName());
        data.put("member.discriminator", member.getUser().getDiscriminator());
        data.put("member.avatar", member.getUser().getEffectiveAvatarUrl());
        data.put("member.id", member.getId());

        // role.-
        Color color = member.getColor();
        data.put("role.color", color == null ? "#ffffff" : String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
        data.put("role.name", member.getRoles().size() == 0 ? "" : member.getRoles().get(0).getName());

        // xp.-
        XPGuild xpGuild = XP.getGuild(guild.getId());
        XPUser xpUser = xpGuild.getUser(member.getId());

        GuildSettings settings = Settings.getGuildSettings(guild.getId());
        String levelMode = settings.getString("xp.levelMode", "default");

        int xp = xpUser.getXP();
        int level = XPUtils.calculateLevel(xp, levelMode);
        int xpToNextLevel = XPUtils.calculateXP(level + 1, levelMode);
        int xpLeft = xpToNextLevel - xp;
        float xpPercent = (xp / (float) xpToNextLevel) * 100;

        data.put("xp.xp", xp + "");
        data.put("xp.left", xpLeft + "");
        data.put("xp.progress", Math.round(xpPercent * 100) / 100f + "");
        data.put("xp.level", level + "");
        data.put("xp.rank", xpGuild.getTop().indexOf(xpUser) + 1 + "");

        // user.-
        member.getUser().retrieveProfile().queue((profile) -> {
            Color profileAccentColor = profile.getAccentColor();
            data.put("user.name", member.getUser().getName());
            data.put("user.discriminator", member.getUser().getDiscriminator());
            data.put("user.avatar", member.getUser().getEffectiveAvatarUrl());
            data.put("user.id", member.getId());
            data.put("user.banner", profile.getBannerUrl());
            data.put("user.accentColor", profileAccentColor == null ? "#ffffff" : ColorUtils.rgbaToHex(profileAccentColor));
            loaded = true;
        });
    }

    public HashMap<String, String> getKeys() {
        return data;
    }
}
