package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.image.ImageRenderer;
import flustix.fluxifyed.image.RenderArgs;
import flustix.fluxifyed.image.RenderData;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.presets.EmbedPresets;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class RankSlashCommand extends SlashCommand {
    public RankSlashCommand() {
        super("rank", "Show your rank on the current server.");
        addOption(OptionType.USER, "user", "The user to show the rank of.", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        Member member = interaction.getOption("user") != null ? Objects.requireNonNull(interaction.getOption("user")).getAsMember() : interaction.getMember();

        SlashCommandUtils.reply(interaction, EmbedPresets.loading.build(), (hook) -> {
            if (!Settings.getGuildSettings(Objects.requireNonNull(interaction.getGuild()).getId()).xpEnabled()) {
                hook.editOriginal(":x: XP is disabled on this server!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
                return;
            }

            if (ImageRenderer.renderImage(new RenderArgs("rank", "rank.png", new RenderData(interaction.getGuild(), member)))) {
                hook.editOriginal("").setFiles(FileUpload.fromData(new File("rank.png"))).complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            } else {
                hook.editOriginal("An error occurred!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            }
        });
    }
}
