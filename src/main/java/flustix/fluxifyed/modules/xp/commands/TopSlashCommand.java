package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
                .setTitle(g.getName() + " - Top 10")
                .setThumbnail(g.getIconUrl())
                .setColor(Colors.ACCENT);

        XPGuild xpGuild = XP.getGuild(g.getId());

        for (int i = 0; i < 10; i++) {
            if (i >= xpGuild.getTop().size()) break;
            XPUser user = xpGuild.getTop().get(i);

            Member member = g.getMemberById(user.getID());
            try {
                if (member == null)
                    member = g.retrieveMemberById(user.getID()).complete();

                if (member.getEffectiveName().equals(member.getUser().getName())) {
                    embed.addField("#" + (i + 1) + " - " + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), user.getXP() + " XP | Level " + XPUtils.calculateLevel(user.getXP(), gs.getString("xp.levelMode", "default")), false);
                } else {
                    embed.addField("#" + (i + 1) + " - " + member.getEffectiveName() + " (" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + ")", user.getXP() + " XP | Level " + XPUtils.calculateLevel(user.getXP(), gs.getString("xp.levelMode", "default")), false);
                }
            } catch (Exception e) {
                embed.addField("#" + (i + 1) + " - " + user.getID(), user.getXP() + " XP | Level " + XPUtils.calculateLevel(user.getXP(), gs.getString("xp.levelMode", "default")), false);
            }
        }

        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setEmbeds(embed.build());
        builder.addActionRow(Button.link("https://fluxifyed.foxes4life.net/leaderboard/" + g.getId(), "View on website"));
        interaction.reply(builder.build()).queue();
    }
}
