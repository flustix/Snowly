package flustix.fluxifyed.modules.welcome.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.sql.ResultSet;

public class WelcomeData {
    private final String guildId;
    private String channelId;
    private WelcomeMessage message;
    private JsonArray roles;

    public WelcomeData(String guildId) {
        this.guildId = guildId;

        load();
    }

    private void load() {
        ResultSet rs = Database.executeQuery("SELECT * FROM welcome WHERE guildid = '" + guildId + "'");
        if (rs == null) return;

        try {
            while (rs.next()) {
                channelId = rs.getString("channelid");
                message = new WelcomeMessage(JsonParser.parseString(rs.getString("message")).getAsJsonObject());
                roles = JsonParser.parseString(rs.getString("roles")).getAsJsonArray();
                Main.LOGGER.info("Loaded welcome data for guild " + guildId + ".");
            }
        } catch (Exception ex) {
            Main.LOGGER.error("Error while loading welcome data for guild " + guildId + "!", ex);
        }
    }

    public void sendMessage(GuildMemberJoinEvent event) {
        if (message == null) return;

        try {
            Main.getBot().getTextChannelById(channelId).sendMessage(message.build(event)).queue();
        } catch (Exception ex) {
            Main.LOGGER.error("Error while sending welcome message for guild " + guildId, ex);
        }

        addRoles(event);
    }

    private void addRoles(GuildMemberJoinEvent event) {
        if (roles == null) return;

        try {
            for (JsonElement roleJson : roles) {
                Role role = event.getGuild().getRoleById(roleJson.getAsString());
                if (role != null) {
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
            }
        } catch (Exception ignored) {}
    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }
}
