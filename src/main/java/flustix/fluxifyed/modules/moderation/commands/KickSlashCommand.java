package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Objects;

public class KickSlashCommand extends SlashCommand {
    public KickSlashCommand() {
        super("kick", "Kicks a user from the server.");
        setPermissionLevel(PermissionLevel.MODERATOR);
        addOption(OptionType.USER, "target", "The user to kick", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping target = interaction.getOption("target");

        if (target == null) {
            Main.LOGGER.warn("Guild Member intent is not enabled!");
            return;
        }

        try {
            Guild guild = interaction.getGuild();
            if (guild == null) return; // you cant even use the commands in dms
            guild.kick(target.getAsUser()).queue((v) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":white_check_mark: Kicked user!")
                        .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                        .addField(":hammer: Kicked by", interaction.getUser().getAsTag(), true)
                        .setColor(Main.accentColor);

                interaction.replyEmbeds(embed.build()).queue();
            }, (error) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":x: Failed to kick user!")
                        .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                        .addField(":x: Error", error.getMessage(), false)
                        .setColor(Main.accentColor);

                interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
            });
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to kick user!")
                    .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                    .addField(":x: Error", e.getMessage(), false)
                    .setColor(Main.accentColor);

            SlashCommandUtils.replyEphemeral(interaction, embed.build());
        }
    }
}
