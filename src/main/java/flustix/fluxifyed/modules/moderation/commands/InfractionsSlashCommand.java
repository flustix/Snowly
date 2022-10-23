package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Autocomplete;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InfractionsSlashCommand extends SlashCommand {
    public InfractionsSlashCommand() {
        super("infractions", "Shows the infractions of a user.", true);
        addPermissions(Permission.MODERATE_MEMBERS);
        addOption(OptionType.USER, "target", "The user to show the infractions of.", true, false);
        addOption(OptionType.STRING, "type", "The type of infractions to show.", false, true);
        addOption(OptionType.INTEGER, "page", "The page of infractions to show.", false, false);
        addAutocomplete("type", new Autocomplete("Warn", "warn"), new Autocomplete("Note", "note"), new Autocomplete("Kick", "kick"), new Autocomplete("Ban", "ban"));
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping targetMapping = interaction.getOption("target");
        OptionMapping typeMapping = interaction.getOption("type");
        OptionMapping pageMapping = interaction.getOption("page");
        if (targetMapping == null) return;

        User target = targetMapping.getAsUser();
        String type = typeMapping == null ? "" : typeMapping.getAsString();
        int page = pageMapping == null ? 1 : pageMapping.getAsInt();
        Guild guild = interaction.getGuild();
        if (guild == null) return;

        try {
            List<String> params = new ArrayList<>();
            params.add(guild.getId());
            params.add(target.getId());

            String query = "SELECT * FROM infractions WHERE guildid = ? AND userid = ?";
            if (!type.isEmpty()) {
                query += " AND type = '?'";
                params.add(type);
            }
            query += " ORDER BY time DESC LIMIT 10 OFFSET ?";

            params.add((page - 1) * 10 + "");

            int count = 0;

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(target.getAsTag(), null, target.getAvatarUrl())
                    .setTitle("Infractions")
                    .setColor(Main.accentColor);

            ResultSet rs = Database.executeQuery(query, params);
            if (rs != null) {
                while (rs.next()) {
                    count++;
                    String infractionType = rs.getString("type");
                    String content = rs.getString("content");
                    String moderator = rs.getString("modid");
                    String time = rs.getString("time");

                    embed.addField(getInfractionType(infractionType) + " <t:" + time + ":f>", "Reason: " + content + "" +
                            " | Moderator: <@" + moderator + ">", false);
                }
            }

            if (count == 0) {
                embed.setDescription("No infractions found.");
            }

            interaction.replyEmbeds(embed.build()).queue();
        } catch (Exception ex) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to show infractions!")
                    .addField(":bust_in_silhouette: User", target.getAsTag(), true)
                    .addField(":x: Error", ex.getMessage(), false)
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }

    static String getInfractionType(String type) {
        return switch (type) {
            case "note" -> ":information_source: Note";
            case "warn" -> ":warning: Warning";
            case "kick" -> ":boot: Kick";
            case "ban" -> ":hammer: Ban";
            default -> "Unknown";
        };
    }
}
