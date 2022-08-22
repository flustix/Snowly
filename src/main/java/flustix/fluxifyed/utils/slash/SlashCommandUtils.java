package flustix.fluxifyed.utils.slash;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.function.Consumer;

public class SlashCommandUtils {
    public static void reply(SlashCommandInteraction interaction, String content) {
        interaction.reply(content).complete();
    }

    public static void reply(SlashCommandInteraction interaction, Message message) {
        interaction.reply(message).complete();
    }

    public static void reply(SlashCommandInteraction interaction, MessageEmbed embed) {
        interaction.replyEmbeds(embed).complete();
    }

    public static void reply(SlashCommandInteraction interaction, String content, Consumer<InteractionHook> callback) {
        InteractionHook hook = interaction.reply(content).complete();
        callback.accept(hook);
    }

    public static void reply(SlashCommandInteraction interaction, Message message, Consumer<InteractionHook> callback) {
        InteractionHook hook = interaction.reply(message).complete();
        callback.accept(hook);
    }

    public static void reply(SlashCommandInteraction interaction, MessageEmbed embed, Consumer<InteractionHook> callback) {
        InteractionHook hook = interaction.replyEmbeds(embed).complete();
        callback.accept(hook);
    }
}
