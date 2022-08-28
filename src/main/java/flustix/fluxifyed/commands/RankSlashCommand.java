package flustix.fluxifyed.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import flustix.fluxifyed.utils.xp.XPUtils;
import flustix.fluxifyed.xp.XP;
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
        SlashCommandUtils.reply(interaction, "Please wait... <a:catItSlaps:935449425722109993>", (hook) -> {
            try {
                XPUser user = XP.getGuild(interaction.getGuild().getId()).getUser(interaction.getMember().getId());

                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(interaction.getUser().getAsTag(), null, interaction.getUser().getAvatarUrl())
                        .setColor(Main.accentColor);

                int level = XPUtils.calculateLevel(user.getXP());
                int nextLevelLeft = XPUtils.calculateXP(level + 1) - user.getXP();

                embed.addField(":1234: XP", user.getXP() + "", true);
                embed.addField(":arrow_up: Level", level + "", true);
                embed.addField(":fast_forward: Next Level", nextLevelLeft + "XP left", true);

                hook.editOriginal(new MessageBuilder().setEmbeds(embed.build()).build()).complete();
            } catch (Exception e) {
                hook.editOriginal("Error while getting your rank.").complete();
            }
        });
    }
}
