package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class TopSlashCommand extends SlashCommand {
    public TopSlashCommand() {
        super("top", "Shows the top 10 users in the server", true);
    }

    public void execute(SlashCommandInteraction interaction) {
        Guild g = interaction.getGuild();
        if (g == null) return;

        if (!Settings.getGuildSettings(interaction.getGuild().getId()).moduleEnabled("xp")) {
            SlashCommandUtils.replyEphemeral(interaction, ":x: XP is disabled on this server!");
            return;
        }

        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent("Here you go!");
        builder.addActionRow(Button.link("https://fluxifyed.foxes4life.net/leaderboard/" + g.getId(), "View on website"));
        SlashCommandUtils.reply(interaction, builder.build());
    }
}
