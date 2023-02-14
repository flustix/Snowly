package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
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

        GuildSettings gs = Settings.getGuildSettings(g.getId());

        if (!gs.getBoolean("xp.enabled", true)) {
            interaction.reply(":x: XP is disabled on this server.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(g.getName() + " - Leaderboard")
                .setThumbnail(g.getIconUrl())
                .setColor(Colors.ACCENT);

        XPGuild xpGuild = XP.getGuild(g.getId());
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            if (i >= xpGuild.getTop().size()) break;
            if (i != 0) content.append("\n");

            XPUser user = xpGuild.getTop().get(i);
            content.append("#").append(i + 1).append(" - <@").append(user.getID()).append("> - ").append(user.getXP()).append(" XP | Level ").append(user.getLevel());
        }

        embed.setDescription(content.toString());

        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setEmbeds(embed.build());
        builder.addActionRow(Button.link("https://fluxifyed.foxes4life.net/leaderboard/" + g.getId(), "View on website"));
        interaction.reply(builder.build()).queue();
    }
}
