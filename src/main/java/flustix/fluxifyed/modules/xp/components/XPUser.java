package flustix.fluxifyed.modules.xp.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.image.ImageRenderer;
import flustix.fluxifyed.image.RenderArgs;
import flustix.fluxifyed.image.RenderData;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XPUser {
    private long xp = 0;
    private long level = 0;
    private long lastUpdate = 0;
    private final String id;
    private final XPGuild guild;

    public XPUser(XPGuild guild, String id) {
        this.guild = guild;
        this.id = id;
    }

    public void addXP(MessageReceivedEvent event) {
        GuildSettings settings = Settings.getGuildSettings(guild.getID());

        int cooldown = settings.getInt("xp.cooldown", 60);
        int randomXpMin = settings.getInt("xp.randomMin", 10);
        int randomXpMax = settings.getInt("xp.randomMax", 20);
        float multiplier = settings.getFloat("xp.multiplier", 1.0f);

        if (lastUpdate + (cooldown * 1000f) > System.currentTimeMillis()) return;

        // check if the random value is in the range
        // if not, use the default values
        if (randomXpMax - randomXpMin + 1 <= 0) {
            Main.LOGGER.error("Invalid xp range for guild " + guild.getID());
            randomXpMin = 10;
            randomXpMax = 20;
        }

        Member member = event.getMember();

        if (member == null) {
            Main.LOGGER.warn("Member intent is disabled!");
            return;
        }

        int xpToAdd = new Random().nextInt(randomXpMax - randomXpMin + 1) + randomXpMin;
        xpToAdd *= getMultiplier(event, event.getMember().getRoles()) * getChannelMultiplier(event.getChannel());
        this.xp += xpToAdd;

        if (calculateLevel() > level) {
            updateLevel();

            String channelid = settings.getString("xp.channel", event.getChannel().getId());
            MessageChannel channel = event.getGuild().getTextChannelById(channelid);

            // backup if we cant find it with the id
            if (channel == null) channel = event.getChannel();

            boolean sameChannel = channel.getId().equals(event.getChannel().getId());
            boolean showLevelUp = Settings.getUserSettings(id).levelUpMessagesEnabled() && settings.getBoolean("xp.levelup", true) || !sameChannel;
            boolean showTooltip = level == 1 && sameChannel;

            if (showLevelUp) {
                try {
                    if (ImageRenderer.renderImage(new RenderArgs("levelup", new RenderData(member.getGuild(), member)))) {
                        if (showTooltip) {
                            channel.sendMessage("*You can disable this message for yourself using </togglelevelup:1023272930295169156> (Toggles this on all servers using this bot)*").addFiles(FileUpload.fromData(new File("levelup.png"))).complete();
                        } else {
                            channel.sendFiles(FileUpload.fromData(new File("levelup.png"))).complete();
                        }
                    } else {
                        // fallback if the image renderer fails
                        EmbedBuilder builder = new EmbedBuilder().setTitle("Level up!").setDescription(member.getAsMention() + " has reached level " + level + "!").setColor(Colors.ACCENT).setThumbnail(member.getUser().getEffectiveAvatarUrl());

                        channel.sendMessageEmbeds(builder.build()).complete();
                    }
                } catch (Exception ex) {
                    // probably cant send a message in that channel
                    // todo: notify the guild owner
                }
            }
        }

        updateRoles(event, settings, member);
        updateXP();
        lastUpdate = System.currentTimeMillis();
    }

    private void updateRoles(MessageReceivedEvent event, GuildSettings settings, Member member) {
        List<XPRole> roles = new ArrayList<>();

        for (XPRole role : guild.getLevelRoles()) {
            if (role.getValue() <= level) {
                roles.add(role);
            }
        }

        // possible options:
        // stack - keep all roles
        // highest - keep only the highest role
        String levelRoleMode = settings.getString("xp.levelRoleMode", "stack");
        boolean isHighestMode = levelRoleMode.equals("highest");

        // if we are in highest mode, remove all roles except the highest one
        if (isHighestMode) {
            roles.sort((o1, o2) -> Float.compare(o2.getValue(), o1.getValue()));

            if (roles.size() > 0) {
                roles = List.of(roles.get(0));
            }
        }

        List<Role> memberRoles = member.getRoles();

        // remove all roles that are not in the list
        for (Role role : memberRoles) {
            XPRole xpRole = null;

            for (XPRole x : guild.getLevelRoles()) {
                if (role.getId().equals(x.getID())) {
                    xpRole = x;
                    break;
                }
            }

            if (xpRole != null) {
                // the role is a level role
                // check if it should be removed
                if (!roles.contains(xpRole)) {
                    member.getGuild().removeRoleFromMember(member, role).complete();
                }
            }
        }

        // finally, add all remaining roles
        for (XPRole xpRole : roles) {
            Role role = event.getGuild().getRoleById(xpRole.getID());

            if (role != null) {
                if (!memberRoles.contains(role)) {
                    member.getGuild().addRoleToMember(member, role).complete();
                }
            }
        }
    }

    public void giveXP(int xp) {
        this.xp += xp;
        updateXP();
        updateLevel();
    }

    public void setXP(int xp) {
        this.xp = xp;
        updateLevel();
    }

    public float getMultiplier(MessageReceivedEvent event, List<Role> roles) {
        GuildSettings settings = Settings.getGuildSettings(guild.getID());
        float multiplier = settings.getFloat("xp.multiplier", 1.0f);

        // multiply: multiply all multipliers together
        // add: add all multipliers together
        // max: use the highest multiplier
        String mode = settings.getString("xp.multiplyMode", "add");

        List<XPRole> hasRoles = new ArrayList<>();

        for (XPRole role : guild.getMultipliers()) {
            if (roles.contains(event.getGuild().getRoleById(role.getID()))) {
                hasRoles.add(role);
            }
        }

        switch (mode) {
            case "multiply" -> {
                for (XPRole role : hasRoles) {
                    multiplier *= role.getValue();
                }
            }
            case "add" -> {
                for (XPRole role : hasRoles) {
                    multiplier += role.getValue();
                }
            }
            case "max" -> {
                for (XPRole role : hasRoles) {
                    multiplier = Math.max(multiplier, role.getValue());
                }
            }
            default -> Main.LOGGER.error("Invalid xp multiply mode for guild " + guild.getID());
        }

        return multiplier;
    }

    private float getChannelMultiplier(Channel channel) {
        for (XPChannel xpChannel : guild.getChannelMultipliers()) {
            if (channel.getId().equals(xpChannel.getID()))
                return xpChannel.getValue();
        }

        return 1;
    }

    void updateLevel() {
        level = calculateLevel();
    }

    private long calculateLevel() {
        String levelMode = Settings.getGuildSettings(guild.getID()).getString("xp.levelMode", "default");
        return XPUtils.calculateLevel(xp, levelMode);
    }

    public void updateXP() {
        Database.executeQuery("INSERT INTO fluxifyed.xp (guildid, userid, xp) VALUES ('" + guild.getID() + "', '" + id + "', " + xp + ") ON DUPLICATE KEY UPDATE xp = " + xp);
    }

    public long getXP() {
        return xp;
    }

    public long getLevel() {
        return calculateLevel();
    }

    public String getID() {
        return id;
    }

    public String getGuildID() {
        return guild.getID();
    }
}
