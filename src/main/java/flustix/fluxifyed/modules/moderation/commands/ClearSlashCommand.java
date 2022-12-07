package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.utils.presets.EmbedPresets;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ClearSlashCommand extends SlashCommand {
    public ClearSlashCommand() {
        super("clear", "Clears the chat.", true);
        addPermissions(Permission.MESSAGE_MANAGE);
        addOption(OptionType.INTEGER, "amount", "The amount of messages to clear.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping amountMapping = interaction.getOption("amount");
        if (amountMapping == null) return;

        int amount = amountMapping.getAsInt();

        interaction.replyEmbeds(EmbedPresets.loading.build()).queue((hook) -> {
            try {
                interaction.getChannel().getHistory().retrievePast(amount).queue((messages)-> {
                    interaction.getChannel().purgeMessages(messages);

                    EmbedBuilder infoEmbed = new EmbedBuilder()
                            .setAuthor(interaction.getUser().getAsTag(), null, interaction.getUser().getAvatarUrl())
                            .setTitle("Cleared " + amount + " messages")
                            .setColor(Colors.ACCENT);

                    EmbedBuilder successEmbed = new EmbedBuilder()
                            .setTitle("Done!")
                            .setColor(Colors.ACCENT);

                    hook.editOriginalEmbeds(successEmbed.build()).queue();
                    interaction.getChannel().sendMessageEmbeds(infoEmbed.build()).queue();
                });
            } catch (Exception e) {
                EmbedBuilder errorEmbed = new EmbedBuilder()
                        .setTitle("Something went wrong!")
                        .setColor(Colors.ACCENT);

                hook.editOriginalEmbeds(errorEmbed.build()).queue();
                Main.LOGGER.error("Something went wrong while clearing messages!", e);
            }
        });
    }
}
