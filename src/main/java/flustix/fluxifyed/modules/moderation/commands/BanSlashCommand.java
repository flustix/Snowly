package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.moderation.ModerationModule;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.concurrent.TimeUnit;

public class BanSlashCommand extends SlashCommand {
    public BanSlashCommand() {
        super("ban", "Bans a user from the server.", true);
        addPermissions(Permission.BAN_MEMBERS);
        addOption(OptionType.USER, "target", "The user to ban", true, false);
        addOption(OptionType.STRING, "reason", "The reason for the ban", false, false);
        addBotPermissions(Permission.BAN_MEMBERS);
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
            guild.ban(target.getAsUser(), 7, TimeUnit.DAYS).reason(reasonText).queue((v) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":white_check_mark: Banned user!")
                        .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                        .addField(":hammer: Banned by", interaction.getUser().getAsTag(), true)
                        .addField(":scroll: Reason", reasonText, false)
                        .setColor(Colors.ACCENT);

                interaction.replyEmbeds(embed.build()).queue();

                Infraction infraction = new Infraction(guild.getId(), target.getAsUser().getId(), interaction.getUser().getId(), "ban", reasonText, System.currentTimeMillis());
                ModerationModule.addInfraction(infraction);
            }, (error) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":x: Failed to ban user!")
                        .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                        .addField(":x: Error", error.getMessage(), false)
                        .setColor(Colors.ACCENT);

                interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
            });
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":x: Failed to ban user!")
                    .addField(":bust_in_silhouette: User", target.getAsUser().getAsTag(), true)
                    .addField(":x: Error", e.getMessage(), false)
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
