package flustix.fluxifyed.modules.reactionroles.components;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.reactionroles.ReactRoles;
import flustix.fluxifyed.utils.CustomMessageUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import java.util.ArrayList;
import java.util.List;

public class ReactMessage {
    public String channelid;
    public String messageid;
    public List<ReactRole> roles;
    public MessageData message;

    public ReactMessage(JsonObject json, boolean isNew) {
        this.channelid = json.get("channelid").getAsString();
        if (!isNew) this.messageid = json.get("messageid").getAsString();
        this.message = new Gson().fromJson(json.get("message"), MessageData.class);

        roles = new ArrayList<>();
        json.get("roles").getAsJsonArray().forEach(role -> {
            JsonObject roleJson = role.getAsJsonObject();
            roles.add(new ReactRole(roleJson.get("role").getAsString(), roleJson.get("emoji").getAsString()));
        });
    }

    public void uploadMessage(String guildid) {
        Database.executeQuery("INSERT INTO reactionRoles (guildid, data) VALUES ('?', '?')", guildid, Database.escape(new Gson().toJson(this)));
    }

    public void updateMessage() {
        TextChannel channel = Main.getBot().getTextChannelById(channelid);
        if (channel == null) return;

        channel.retrieveMessageById(messageid).queue((msg) -> msg.editMessage(CustomMessageUtils.createEdit(message)).queue());
    }

    public void sendMessage(MessageChannel channel) {
        channel.sendMessage(CustomMessageUtils.create(message)).queue((message) -> {
            if (messageid == null) {
                messageid = message.getId();
                ReactRoles.addReactMessage(this);
                uploadMessage(message.getGuild().getId());
            }

            for (ReactRole role : roles) {
                react(message, role);
            }
        });
    }

    public void react(Message msg, ReactRole role) {
        if (role.emoji.startsWith("U+")) {
            msg.addReaction(Emoji.fromUnicode(role.emoji)).queue();
        } else {
            RichCustomEmoji emote = Main.getBot().getEmojiById(role.emoji);
            if (emote != null)
                msg.addReaction(emote).queue();
        }
    }
}
