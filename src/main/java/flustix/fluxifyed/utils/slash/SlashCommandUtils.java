package flustix.fluxifyed.utils.slash;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.function.Consumer;

public class SlashCommandUtils {
    public static void reply(SlashCommandInteraction interaction, String content) {
        reply(interaction, new MessageBuilder().setContent(content).build());
    }

    public static void reply(SlashCommandInteraction interaction, Message message) {
        reply(interaction, message, (hook)-> {});
    }

    public static void reply(SlashCommandInteraction interaction, MessageEmbed embed) {
        reply(interaction, new MessageBuilder().setEmbeds(embed).build());
    }

    public static void reply(SlashCommandInteraction interaction, String content, Consumer<InteractionHook> callback) {
        reply(interaction, new MessageBuilder().setContent(content).build(), callback);
    }

    public static void reply(SlashCommandInteraction interaction, Message message, Consumer<InteractionHook> callback) {
        InteractionHook hook = interaction.reply(message).complete();
        callback.accept(hook);
    }

    public static void reply(SlashCommandInteraction interaction, MessageEmbed embed, Consumer<InteractionHook> callback) {
        reply(interaction, new MessageBuilder().setEmbeds(embed).build(), callback);
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, String content) {
        replyEphemeral(interaction, new MessageBuilder().setContent(content).build());
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, MessageEmbed embed) {
        replyEphemeral(interaction, new MessageBuilder().setEmbeds(embed).build());
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, Message message) {
        replyEphemeral(interaction, message, (hook)-> {});
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, String content, Consumer<InteractionHook> callback) {
        replyEphemeral(interaction, new MessageBuilder().setContent(content).build(), callback);
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, MessageEmbed embed, Consumer<InteractionHook> callback) {
        replyEphemeral(interaction, new MessageBuilder().setEmbeds(embed).build(), callback);
    }

    public static void replyEphemeral(SlashCommandInteraction interaction, Message message, Consumer<InteractionHook> callback) {
        InteractionHook hook = interaction.reply(message).setEphemeral(true).complete();
        callback.accept(hook);
    }
}
