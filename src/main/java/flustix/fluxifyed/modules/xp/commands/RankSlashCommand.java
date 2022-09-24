package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.modules.xp.images.RankImage;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.presets.EmbedPresets;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;

public class RankSlashCommand extends SlashCommand {
    public RankSlashCommand() {
        super("rank", "Show your rank on the current server.");
        addOption(OptionType.USER, "user", "The user to show the rank of.", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        Member member = interaction.getOption("user") != null ? interaction.getOption("user").getAsMember() : interaction.getMember();

        SlashCommandUtils.reply(interaction, EmbedPresets.loading.build(), (hook) -> {
            XPGuild guild = XP.getGuild(interaction.getGuild().getId());
            if (!Settings.getGuildSettings(interaction.getGuild().getId()).xpEnabled()) {
                hook.editOriginal(":x: XP is disabled on this server!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
                return;
            }

            if (RankImage.create(
                    member.getEffectiveAvatarUrl() + "?size=256",
                    member.getEffectiveName(),
                    interaction.getGuild().getIconUrl() + "?size=128",
                    interaction.getGuild().getName(),
                    guild.getUser(member.getId()).getXP(),
                    guild.getTop().indexOf(guild.getUser(member.getId())) + 1,
                    member.getColor())) {
                hook.editOriginal("").setFiles(FileUpload.fromData(RankImage.file)).complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            } else {
                hook.editOriginal("An error occurred!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            }
        });
    }
}