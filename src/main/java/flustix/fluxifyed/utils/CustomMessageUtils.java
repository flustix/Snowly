package flustix.fluxifyed.utils;

import flustix.fluxifyed.components.message.FieldData;
import flustix.fluxifyed.components.message.MessageData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;

public class CustomMessageUtils {
    public static MessageCreateData create(MessageData message) {
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

        return msg.build();
    }

    public static MessageEditData createEdit(MessageData message) {
        return MessageEditData.fromCreateData(create(message));
    }
}
