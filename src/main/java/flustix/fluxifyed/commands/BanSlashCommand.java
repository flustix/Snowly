package flustix.fluxifyed.commands;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class BanSlashCommand extends SlashCommand {
    public BanSlashCommand() {
        super("ban", "Bans people");
        this.addOption(OptionType.USER, "target", "The user to ban", true, false);
        this.addOption(OptionType.STRING, "reason", "The reason for the ban", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping target = interaction.getOption("target");
        OptionMapping reason = interaction.getOption("reason");

        if (target == null) {
            SlashCommandUtils.reply(interaction, "how the fuck did you even get here");
            return;
        }

        String reasonText = reason == null ? "No reason" : reason.getAsString();

        try {
            interaction.getGuild().ban(target.getAsUser(), 7, reasonText).queue((v) -> {
                interaction.reply(":white_check_mark: Banned " + target.getAsUser().getAsTag() + " for " + reasonText).queue();
            }, (error) -> {
                interaction.reply(":x: I can't ban that user: " + error.getMessage()).queue();
            });
        } catch (Exception e) {
            SlashCommandUtils.reply(interaction, ":x: Failed to ban user: " + e.getMessage());
        }
    }
}
