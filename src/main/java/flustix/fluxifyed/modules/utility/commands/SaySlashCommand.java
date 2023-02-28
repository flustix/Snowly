package flustix.fluxifyed.modules.utility.commands;

import com.google.gson.Gson;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.utils.CustomMessageUtils;
import flustix.fluxifyed.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class SaySlashCommand extends SlashCommand {
    public SaySlashCommand() {
        super("say", false);
        addOption(OptionType.STRING, "message", "The message to say", true, false);
        addOption(OptionType.CHANNEL, "channel", "The channel to say the message in", false, false);
        addOption(OptionType.STRING, "reply", "The message to reply to (as ID)", false, false);
        addPermissions(Permission.MESSAGE_MANAGE);
    }

    public void execute(SlashCommandInteraction event) {
        OptionMapping messageMapping = event.getOption("message");
        OptionMapping channelMapping = event.getOption("channel");
        OptionMapping replyMapping = event.getOption("reply");

        if (messageMapping == null) {
            event.reply("You must provide a message to say!").setEphemeral(true).queue();
            return;
        }

        String messageJson = messageMapping.getAsString();
        MessageData message = new Gson().fromJson(messageJson, MessageData.class);

        MessageCreateData messageCreateData = CustomMessageUtils.create(message);

        MessageChannel channel = null;

        if (channelMapping != null) {
            Channel channelObj = channelMapping.getAsChannel();

            if (channelObj instanceof MessageChannel) {
                channel = (TextChannel) channelObj;
            } else {
                event.reply("The channel you provided is not a text channel!").setEphemeral(true).queue();
                return;
            }
        }

        if (channel == null) {
            channel = event.getChannel();
        }

        if (replyMapping != null) {
            String replyId = replyMapping.getAsString();
            channel.sendMessage(messageCreateData).setMessageReference(replyId).failOnInvalidReply(true).queue(msg -> event.reply("Message sent!").setEphemeral(true).queue(), e -> event.reply("The reply you provided is not a valid message ID!").setEphemeral(true).queue());
        } else {
            channel.sendMessage(messageCreateData).queue(msg -> event.reply("Message sent!").setEphemeral(true).queue(), e -> event.reply("An error occurred while sending the message!\n" + MessageUtils.exceptionToCode(e)).setEphemeral(true).queue());
        }
    }
}
