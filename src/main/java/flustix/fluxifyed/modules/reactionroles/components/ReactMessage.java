package flustix.fluxifyed.modules.reactionroles.components;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.message.FieldData;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.reactionroles.ReactRoles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;
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

        channel.retrieveMessageById(messageid).queue((msg) -> {
            msg.editMessage(MessageEditData.fromCreateData(getMessage().build())).queue();
        });
    }

    public void sendMessage(MessageChannel channel) {
        channel.sendMessage(getMessage().build()).queue((message) -> {
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

    public MessageCreateBuilder getMessage() {
        MessageCreateBuilder msg = new MessageCreateBuilder();

        if (message.content != null)
            msg.setContent(message.content);

        if (message.embed != null) {
            EmbedBuilder embed = new EmbedBuilder();

            if (!message.embed.title.isEmpty()) {
                if (!message.embed.url.isEmpty())
                    embed.setTitle(message.embed.title, message.embed.url);
                else
                    embed.setTitle(message.embed.title);
            }

            if (!message.embed.title.isEmpty())
                embed.setDescription(message.embed.description);

            if (!message.embed.color.isEmpty())
                embed.setColor(Color.decode(message.embed.color));

            if (!message.embed.image.isEmpty())
                embed.setImage(message.embed.image);

            if (!message.embed.thumbnail.isEmpty())
                embed.setThumbnail(message.embed.thumbnail);

            if (message.embed.fields != null) {
                for (FieldData field : message.embed.fields) {
                    if (!field.name.isEmpty() && !field.value.isEmpty())
                        embed.addField(field.name, field.value, field.inline);
                }
            }

            if (message.embed.author != null) {
                if (!message.embed.author.name.isEmpty()) {
                    if (!message.embed.author.url.isEmpty()) {
                        if (!message.embed.author.icon.isEmpty())
                            embed.setAuthor(message.embed.author.name, message.embed.author.url, message.embed.author.icon);
                        else
                            embed.setAuthor(message.embed.author.name, message.embed.author.url);
                    } else {
                        embed.setAuthor(message.embed.author.name);
                    }
                }
            }

            if (message.embed.footer != null) {
                if (!message.embed.footer.text.isEmpty()) {
                    if (!message.embed.footer.icon.isEmpty()) {
                        embed.setFooter(message.embed.footer.text, message.embed.footer.icon);
                    } else {
                        embed.setFooter(message.embed.footer.text);
                    }
                }
            }

            msg.setEmbeds(embed.build());
        }

        return msg;
    }
}
