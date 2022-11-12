package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.temporal.ChronoField;

public class KickSlashCommand extends SlashCommand {
    public KickSlashCommand() {
        super("kick", "Kicks a user from the server.", true);
        addPermissions(Permission.KICK_MEMBERS);
        addOption(OptionType.USER, "target", "The user to kick", true, false);
        addOption(OptionType.STRING, "reason", "The reason for the ban", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping target = interaction.getOption("target");
        OptionMapping reason = interaction.getOption("reason");

        if (target == null) {
            Main.LOGGER.warn("Guild Member intent is not enabled!");
            return;
        }

        String reasonText = reason == null ? "No reason" : reason.getAsString();

        try {
            Guild guild = interaction.getGuild();
            if (guild == null) return; // you cant even use the commands in dms
            guild.kick(target.getAsUser()).queue((v) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":white_check_mark: Kicked user!")
                        .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                        .addField(":hammer: Kicked by", interaction.getUser().getAsTag(), true)
                        .addField(":scroll: Reason", reasonText, false)
                        .setColor(Main.accentColor);

                interaction.replyEmbeds(embed.build()).queue();
                Database.executeQuery("INSERT INTO infractions (guildid, userid, modid, type, content, time) VALUES (?, ?, ?, '?', '?', ?)", guild.getId(), target.getAsUser().getId(), interaction.getUser().getId(), "kick", Database.escape(reasonText), interaction.getTimeCreated().getLong(ChronoField.INSTANT_SECONDS) + "");
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

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
