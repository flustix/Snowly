package flustix.fluxifyed.image;

import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.HashMap;

public class RenderData {
    Guild guild;
    Member member;

    HashMap<String, String> data = new HashMap<>();

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
        data.put("role.name", member.getRoles().get(0).getName());

        // xp.-
        XPGuild xpGuild = XP.getGuild(guild.getId());
        XPUser xpUser = xpGuild.getUser(member.getId());

        int xp = xpUser.getXP();
        int level = XPUtils.calculateLevel(xp);
        int xpLeft = XPUtils.calculateXP(level + 1) - xp;

        data.put("xp.xp", xp + "");
        data.put("xp.xpleft", xpLeft + "");
        data.put("xp.level", level + "");
        data.put("xp.rank", xpGuild.getTop().indexOf(xpUser) + 1 + "");
    }

    public HashMap<String, String> getKeys() {
        return data;
    }
}
