package flustix.fluxifyed.modules.xp.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.images.LevelUpImage;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.xp.XPUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

public class XPUser {
    private int xp = 0;
    private final String id;
    private final String gid;
    private int level = 0;

    private long lastUpdate = 0;

    public XPUser(String gid, String id) {
        this.id = id;
        this.gid = gid;
    }

    public void addXP(int xp, MessageReceivedEvent event) {
        if (lastUpdate + 60000 > System.currentTimeMillis())
            return;

        this.xp += xp;

        if (XPUtils.calculateLevel(this.xp) > level) {
            updateLevel();

            if (Settings.getUserSettings(id).levelUpMessagesEnabled()) {
                Member member = event.getMember();

                if (member == null) {
                    Main.LOGGER.warn("Member is null?? (XPUser.addXP:LevelUPMessage) (" + gid + ":" + id + ")");
                    return;
                }

                if (LevelUpImage.create(
                        member.getEffectiveAvatarUrl() + "?size=256",
                        event.getMember().getEffectiveName(),
                        level
                )) {

                    event.getChannel().sendFiles(FileUpload.fromData(LevelUpImage.file)).complete();
                } else {
                    event.getChannel().sendMessage("Congrats " + event.getAuthor().getAsMention() + " you leveled up to level " + level + "!").complete();
                }
            }
        }

        for (XPRole role : XP.getGuild(gid).getRoles()) {
            if (level >= role.getLevel()) {
                Member member = event.getMember();

                if (member == null) {
                    Main.LOGGER.warn("Member is null?? (XPUser.addXP:RoleCheck) (" + gid + ":" + id + ")");
                    return;
                }

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

    void updateLevel() {
        level = XPUtils.calculateLevel(xp);
    }

    public void updateXP() {
        Database.executeQuery("INSERT INTO xp (guildid, userid, xp) VALUES ('" + gid + "', '" + id + "', " + xp + ") ON DUPLICATE KEY UPDATE xp = " + this.xp);
    }

    public int getXP() {
        return xp;
    }

    public String getID() {
        return id;
    }

    public String getGuildID() {
        return gid;
    }
}
