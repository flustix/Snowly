package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.settings.UserSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ToggleLevelUPSlashCommand extends SlashCommand {
    public ToggleLevelUPSlashCommand() {
        super("togglelevelup", "Toggle level up messages globally.");
    }

    public void execute(SlashCommandInteraction interaction) {
        try {
            UserSettings user = Settings.getUserSettings(interaction.getUser().getId());

            user.setLevelUpMessagesEnabled(!user.levelUpMessagesEnabled());

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Toggled Level Up Messages")
                    .addField(":1234: Level Up Messages", user.levelUpMessagesEnabled() ? "Enabled" : "Disabled", true)
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } catch (Exception e) {
            interaction.reply("An error occurred while toggling level up messages.").setEphemeral(true).queue();
        }
    }
}
