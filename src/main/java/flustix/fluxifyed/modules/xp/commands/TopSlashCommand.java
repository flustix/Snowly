package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.images.TopImage;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.presets.EmbedPresets;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;
import java.util.Objects;

public class TopSlashCommand extends SlashCommand {
    public TopSlashCommand() {
        super("top", "Shows the top 10 users in the server");
    }

    public void execute(SlashCommandInteraction interaction) {
        SlashCommandUtils.reply(interaction, EmbedPresets.loading.build(), (hook) -> {
            XPGuild guild = XP.getGuild(Objects.requireNonNull(interaction.getGuild()).getId());
            if (!Settings.getGuildSettings(interaction.getGuild().getId()).xpEnabled()) {
                hook.editOriginal(":x: XP is disabled on this server!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
                return;
            }

            if (TopImage.create(guild)) {
                hook.editOriginal("").setFiles(FileUpload.fromData(TopImage.file)).complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            } else {
                hook.editOriginal("An error occurred!").complete();
                hook.editOriginalEmbeds(new ArrayList<>()).complete();
            }
        });
    }
}
