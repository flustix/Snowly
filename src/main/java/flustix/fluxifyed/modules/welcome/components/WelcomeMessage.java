package flustix.fluxifyed.modules.welcome.components;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;

public class WelcomeMessage {
    private final String content;
    private final EmbedData embed;

    public WelcomeMessage(JsonObject data) {
        content = data.get("content").getAsString();
        embed = new EmbedData(data.get("embed").getAsJsonObject());
    }

    public MessageCreateData build(GuildMemberJoinEvent event) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent(replacePlaceholders(content, event));
        builder.setEmbeds(embed.build(event));
        return builder.build();
    }

    public static String replacePlaceholders(String content, GuildMemberJoinEvent event) {
        return content.replace("{member.name}", event.getMember().getEffectiveName())
                .replace("{member.avatar}", event.getMember().getEffectiveAvatarUrl())
                .replace("{member.tag}", event.getMember().getUser().getAsTag())
                .replace("{member.ping}", event.getMember().getAsMention());
    }

    private static class EmbedData {
        public String title;
        public String description;
        public String color;
        public String thumbnail;
        public String image;

        public EmbedData(JsonObject data) {
            title = data.get("title").getAsString();
            description = data.get("description").getAsString();
            color = data.get("color").getAsString();
            thumbnail = data.get("thumbnail").getAsString();
            image = data.get("image").getAsString();
        }

        public MessageEmbed build(GuildMemberJoinEvent event) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(replacePlaceholders(title, event));
            builder.setDescription(replacePlaceholders(description, event));
            builder.setColor(Color.decode(color));
            builder.setThumbnail(replacePlaceholders(thumbnail, event));
            builder.setImage(replacePlaceholders(image, event));
            return builder.build();
        }
    }
}
