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

public class NoteSlashCommand extends SlashCommand {
    public NoteSlashCommand() {
        super("note", "Adds a note to a user.");
        addPermissions(Permission.MODERATE_MEMBERS);
        addOption(OptionType.USER, "user", "The user to add a note to.", true, false);
        addOption(OptionType.STRING, "note", "The note.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userMapping = interaction.getOption("user");
        OptionMapping noteMapping = interaction.getOption("note");

        if (userMapping == null || noteMapping == null) return;

        User user = userMapping.getAsUser();
        String note = noteMapping.getAsString();
        Guild guild = interaction.getGuild();

        if (guild == null) return;

        try {
            Database.executeQuery("INSERT INTO infractions (guildid, userid, modid, type, content, time) VALUES (?, ?, ?, '?', '?', ?)", guild.getId(), user.getId(), interaction.getUser().getId(), "note", Database.escape(note), interaction.getTimeCreated().getLong(ChronoField.INSTANT_SECONDS) + "");

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":information_source: Added note to user " + user.getAsTag() + "")
                    .addField(":scroll: Note", note, false)
                    .setFooter("ID: " + user.getId())
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } catch (Exception ex) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to add note to user!")
                    .addField(":x: Error", ex.toString(), false)
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
