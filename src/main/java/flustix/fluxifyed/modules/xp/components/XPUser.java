package flustix.fluxifyed.modules.xp.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.image.ImageRenderer;
import flustix.fluxifyed.image.RenderArgs;
import flustix.fluxifyed.image.RenderData;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XPUser {
    private int xp = 0;
    private int level = 0;
    private final String id;
    private final XPGuild guild;
    private long lastUpdate = 0;

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

        if (lastUpdate + (cooldown * 1000f) > System.currentTimeMillis())
            return;

        // check if the random value is in the range
        // if not, use the default values
        if (randomXpMax - randomXpMin + 1 <= 0) {
            Main.LOGGER.error("Invalid xp range for guild " + guild.getID());
            randomXpMin = 10;
            randomXpMax = 20;
        }

        Member member = event.getMember();

        if (member == null) {
            Main.LOGGER.warn("Member is null?? (XPUser.addXP:LevelUPMessage) (" + guild.getID() + ":" + id + ")");
            return;
        }

        int xpToAdd = new Random().nextInt(randomXpMax - randomXpMin + 1) + randomXpMin;
        xpToAdd *= getMultiplier(event, event.getMember().getRoles());
        this.xp += xpToAdd;

        if (XPUtils.calculateLevel(this.xp) > level) {
            updateLevel();

            if (Settings.getUserSettings(id).levelUpMessagesEnabled() && settings.getBoolean("xp.levelup", true)) {
                String channelid = settings.getString("xp.channel", "");

                if (channelid.isEmpty())
                    channelid = event.getChannel().getId();

                MessageChannel channel = event.getGuild().getTextChannelById(channelid);

                // backup if we cant find it with the id
                if (channel == null)
                    channel = event.getChannel();

                try {
                    if (ImageRenderer.renderImage(new RenderArgs("levelup", "levelup.png", new RenderData(member.getGuild(), member)))) {
                        if (level == 1) {
                            channel.sendMessage("*You can disable this message for yourself using </togglelevelup:1023272930295169156> (Toggles this on all servers using this bot)*").addFiles(FileUpload.fromData(new File("levelup.png"))).complete();
                        } else {
                            channel.sendFiles(FileUpload.fromData(new File("levelup.png"))).complete();
                        }
                    } else {
                        channel.sendMessage("Congrats " + event.getAuthor().getAsMention() + " you leveled up to level " + level + "!").complete();
                    }
                } catch (Exception ex) {
                    // probably cant send a message in that channel
                    // todo: notify the guild owner
                }

            }
        }

        for (XPRole role : guild.getLevelRoles()) {
            if (level >= role.getValue()) {
                Role r = event.getGuild().getRoleById(role.getID());

                if (r == null) return; // doesn't exist anymore i think

                if (!member.getRoles().contains(r)) {
                    event.getGuild().addRoleToMember(member, r).complete();
                }
            }
        }

        updateXP();
        lastUpdate = System.currentTimeMillis();
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

    void updateLevel() {
        level = XPUtils.calculateLevel(xp);
    }

    public void updateXP() {
        Database.executeQuery("INSERT INTO xp (guildid, userid, xp) VALUES ('" + guild.getID() + "', '" + id + "', " + xp + ") ON DUPLICATE KEY UPDATE xp = " + xp);
    }

    public int getXP() {
        return xp;
    }

    public String getID() {
        return id;
    }

    public String getGuildID() {
        return guild.getID();
    }
}
