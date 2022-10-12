package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Objects;

public class ToggleXPSlashCommand extends SlashCommand {
    public ToggleXPSlashCommand() {
        super("togglexp", "Toggle XP on the server.");
        setPermissionLevel(PermissionLevel.ADMIN);
    }

    public void execute(SlashCommandInteraction interaction) {
        try {
            Guild g = interaction.getGuild();
            if (g == null) return;
            GuildSettings guild = Settings.getGuildSettings(g.getId());

            guild.setModuleEnabled("xp", !guild.moduleEnabled("xp"));

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Toggled XP")
                    .addField(":1234: XP", guild.moduleEnabled("xp") ? "Enabled" : "Disabled", true)
                    .setColor(Main.accentColor);

            SlashCommandUtils.reply(interaction, embed.build());
        } catch (Exception e) {
            SlashCommandUtils.replyEphemeral(interaction, "An error occurred while toggling xp.");
        }
    }
}
