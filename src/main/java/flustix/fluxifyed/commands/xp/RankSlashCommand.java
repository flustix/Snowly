package flustix.fluxifyed.commands.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.images.xp.RankImage;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import flustix.fluxifyed.xp.XP;
import flustix.fluxifyed.xp.types.XPGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.ArrayList;

public class RankSlashCommand extends SlashCommand {
    public RankSlashCommand() {
        super("rank", "Show your rank on the current server.");
    }

    public void execute(SlashCommandInteraction interaction) {
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Loading...", null, interaction.getJDA().getSelfUser().getAvatarUrl())
                .setDescription("Please wait...")
                .setColor(Main.accentColor);

        SlashCommandUtils.reply(interaction, embed.build(), (hook) -> {
            XPGuild guild = XP.getGuild(interaction.getGuild().getId());
            if (!guild.isXpEnabled) {
                hook.editOriginal(":x: XP is disabled on this server!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
                return;
            }

            if (RankImage.create(
                    interaction.getMember().getEffectiveAvatarUrl() + "?size=256",
                    interaction.getMember().getEffectiveName(),
                    interaction.getGuild().getIconUrl() + "?size=128",
                    interaction.getGuild().getName(),
                    guild.getUser(interaction.getMember().getId()).getXP())) {
                hook.editOriginal("").addFile(RankImage.file).complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            } else {
                hook.editOriginal("An error occurred!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            }
        });
    }
}
