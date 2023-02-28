package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.components.Autocomplete;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.moderation.ModerationModule;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import flustix.fluxifyed.modules.moderation.types.InfractionType;
import flustix.fluxifyed.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class InfractionsSlashCommand extends SlashCommand {
    public InfractionsSlashCommand() {
        super("infractions", true);
        addPermissions(Permission.MODERATE_MEMBERS);
        addOption(OptionType.USER, "target", "The user to show the infractions of.", true, false);
        addOption(OptionType.STRING, "type", "The type of infractions to show.", false, true);
        addOption(OptionType.INTEGER, "page", "The page of infractions to show.", false, false);
        addAutocomplete("type", new Autocomplete("Warn", "warn"), new Autocomplete("Note", "note"), new Autocomplete("Kick", "kick"), new Autocomplete("Ban", "ban"), new Autocomplete("Mute", "mute"));
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
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(target.getAsTag(), null, target.getAvatarUrl())
                    .setTitle("Infractions")
                    .setColor(Colors.ACCENT);

            List<Infraction> infractions;

            if (type.isEmpty()) {
                infractions = ModerationModule.getInfractions(guild.getId(), target.getId());
            } else {
                infractions = ModerationModule.getInfractions(guild.getId(), target.getId(), InfractionType.fromString(type));
            }

            int pages = (int) Math.ceil(infractions.size() / 10.0);
            page = Math.min(page, pages);

            if (infractions.size() == 0) {
                if (type.isEmpty())
                    embed.setDescription("No infractions found.");
                else
                    embed.setDescription("No infractions of type **" + type + "** found.");
            } else {
                embed.setFooter("Page " + page + "/" + pages);
            }

            int index = 0;

            for (Infraction infraction : infractions) {
                if (index >= (page - 1) * 10 && index < page * 10) {
                    embed.addField(getInfractionType(infraction.getType()) + " <t:" + (infraction.getTime() / 1000) + ":f>", "**Reason** " + infraction.getReason() + "\n**Moderator** <@" + infraction.getModerator() + ">\n**ID** " + infraction.getId(), true);
                }
                index++;
            }

            interaction.replyEmbeds(embed.build()).queue();
        } catch (Exception ex) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to show infractions!")
                    .addField(":bust_in_silhouette: User", target.getAsTag(), true)
                    .addField(":x: Error", MessageUtils.exceptionToCode(ex), false)
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }

    static String getInfractionType(InfractionType type) {
        return switch (type) {
            case NOTE -> ":information_source: Note";
            case WARN -> ":warning: Warning";
            case KICK -> ":boot: Kick";
            case BAN -> ":hammer: Ban";
            case MUTE -> ":mute: Mute";
        };
    }
}
