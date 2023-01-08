package flustix.fluxifyed.modules.welcome.components;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.image.RenderData;
import flustix.fluxifyed.utils.CustomMessageUtils;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.sql.ResultSet;
import java.util.Map;

public class WelcomeData {
    private final String guildId;
    private String channelId;
    private String messageRaw;
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
                messageRaw = rs.getString("message");
                roles = JsonParser.parseString(rs.getString("roles")).getAsJsonArray();
            }
        } catch (Exception ex) {
            Main.LOGGER.error("Error while loading welcome data for guild " + guildId + "!", ex);
        }
    }

    public void sendMessage(GuildMemberJoinEvent event) {
        if (messageRaw == null) return;

        try {
            TextChannel channel = event.getGuild().getTextChannelById(channelId);
            if (channel == null) return;

            // use this so we can use the replaceable variables
            RenderData renderData = new RenderData(event.getGuild(), event.getMember());
            String message = messageRaw;

            while (!renderData.loaded) {
                Thread.sleep(10); // wait until the data is loaded
            }

            for (Map.Entry<String, String> entry : renderData.getKeys().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (value == null) continue;

                message = message.replace("{" + key + "}", value);
            }

            channel.sendMessage(CustomMessageUtils.create(new Gson().fromJson(message, MessageData.class))).queue();
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
