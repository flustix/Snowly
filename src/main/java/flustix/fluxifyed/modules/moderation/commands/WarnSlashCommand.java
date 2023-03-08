package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.moderation.ModerationModule;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import flustix.fluxifyed.modules.moderation.types.InfractionType;
import flustix.fluxifyed.utils.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class WarnSlashCommand extends SlashCommand {
    public WarnSlashCommand() {
        super("warn", true);
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

            Infraction infraction = new Infraction(guild.getId(), user.getId(), interaction.getUser().getId(), "warn", reason, System.currentTimeMillis());
            ModerationModule.addInfraction(infraction);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":warning: Warned " + user.getAsTag())
                    .addField(":bust_in_silhouette: Moderator", interaction.getUser().getAsMention(), true)
                    .addField(":scroll: Reason", reason, false)
                    .setFooter("ID: " + user.getId())
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();

            MessageCreateData data = new MessageCreateBuilder()
                    .setEmbeds(new EmbedBuilder()
                            .setTitle(":warning: You have been warned in " + guild.getName())
                            .addField(":scroll: Reason", reason, false)
                            .setFooter("You have " + ModerationModule.getInfractions(user.getId(), guild.getId()).stream().filter(i -> i.getType() == InfractionType.WARN).toList().size() + " warning(s).")
                            .setColor(Colors.WARNING)
                            .build()).build();

            UserUtils.directMessage(user, data);
        } catch (Exception ex) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to warn user!")
                    .addField(":bust_in_silhouette: User", user.getAsTag(), true)
                    .addField(":x: Error", ex.toString(), false)
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
