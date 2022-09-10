package flustix.fluxifyed.commands.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import flustix.fluxifyed.utils.xp.XPUtils;
import flustix.fluxifyed.xp.XP;
import flustix.fluxifyed.xp.types.XPGuild;
import flustix.fluxifyed.xp.types.XPUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class RankSlashCommand extends SlashCommand {
    public RankSlashCommand() {
        super("rank", "Show your rank on the current server.");
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        try {
            XPGuild guild = XP.getGuild(interaction.getGuild().getId());

            if (!guild.isXpEnabled) {
                SlashCommandUtils.replyEphemeral(interaction, ":x: XP is disabled on this server!");
                return;
            }

            XPUser user = guild.getUser(interaction.getMember().getId());

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(interaction.getUser().getAsTag(), null, interaction.getUser().getAvatarUrl())
                    .setColor(Main.accentColor);

            int level = XPUtils.calculateLevel(user.getXP());
            int nextLevelLeft = XPUtils.calculateXP(level + 1) - user.getXP();

            embed.addField(":1234: XP", user.getXP() + "", true);
            embed.addField(":arrow_up: Level", level + "", true);
            embed.addField(":fast_forward: Next Level", nextLevelLeft + "XP left", true);

            SlashCommandUtils.reply(interaction, new MessageBuilder().setEmbeds(embed.build()).build());
        } catch (Exception e) {
            SlashCommandUtils.replyEphemeral(interaction, "Error while getting your rank.");
        }
    }
}
