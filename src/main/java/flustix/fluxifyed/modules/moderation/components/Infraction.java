package flustix.fluxifyed.modules.moderation.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.moderation.types.InfractionType;

import java.sql.ResultSet;
import java.util.Date;

public class Infraction {
    private int id;
    private final InfractionType type;

    private final String guild;
    private final String user;
    private final String moderator;
    private final String reason;
    private final long time;

    public Infraction(String guild, String user, String moderator, String type, String reason, long time) {
        this.guild = guild;
        this.user = user;
        this.moderator = moderator;
        this.type = InfractionType.fromString(type);
        this.reason = reason;
        this.time = time;
    }

    public void addToDatabase() {
        Database.executeQuery("INSERT INTO infractions (guildid, userid, modid, type, content, time) VALUES ('?', '?', '?', '?', '?', '?')", guild, user, moderator, type.toString(), Database.escape(reason), time);

        ResultSet rs = Database.executeQuery("SELECT id FROM infractions WHERE guildid = '?' AND userid = '?' AND time = '?'", guild, user, time);
        if (rs == null) return;

        try {
            if (rs.next()) {
                setId(rs.getInt("id"));
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while retrieving infraction id from database", e);
        }
    }

    public void removeFromDatabase() {
        Database.executeQuery("DELETE FROM infractions WHERE id = '?'", id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public InfractionType getType() {
        return type;
    }

    public String getGuild() {
        return guild;
    }

    public String getUser() {
        return user;
    }

    public String getModerator() {
        return moderator;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public Date getDate() {
        return new Date(time);
    }
}
