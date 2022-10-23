package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.temporal.ChronoField;

public class WarnSlashCommand extends SlashCommand {
    public WarnSlashCommand() {
        super("warn", "Warn a user.");
        addPermissions(Permission.MODERATE_MEMBERS);
        addOption(OptionType.USER, "user", "The user to warn.", true, false);
        addOption(OptionType.STRING, "reason", "The reason for the warning.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userMapping = interaction.getOption("user");
        OptionMapping reasonMapping = interaction.getOption("reason");

        if (userMapping == null || reasonMapping == null) return;

        User user = userMapping.getAsUser();
        String reason = reasonMapping.getAsString();
        Guild guild = interaction.getGuild();

        if (guild == null) return;

        try {
            Database.executeQuery("INSERT INTO infractions (guildid, userid, modid, type, content, time) VALUES (?, ?, ?, '?', '?', ?)", guild.getId(), user.getId(), interaction.getUser().getId(), "warn", Database.escape(reason), interaction.getTimeCreated().getLong(ChronoField.INSTANT_SECONDS) + "");

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":Warning: Warned " + user.getAsTag())
                    .addField(":bust_in_silhouette: Moderator", interaction.getUser().getAsMention(), true)
                    .addField(":scroll: Reason", reason, false)
                    .setFooter("ID: " + user.getId())
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } catch (Exception ex) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to warn user!")
                    .addField(":bust_in_silhouette: User", user.getAsTag(), true)
                    .addField(":x: Error", ex.toString(), false)
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
