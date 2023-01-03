package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

// god i love class names sometimes
public class ToggleServerLevelUpSlashCommand extends SlashCommand {
    public ToggleServerLevelUpSlashCommand() {
        super("toggleserverlevelup", "Toggles the level up messages for the server", true);
        addPermissions(Permission.MANAGE_SERVER);
    }

    public void execute(SlashCommandInteraction interaction) {
        try {
            Guild g = interaction.getGuild();
            if (g == null) return;
            GuildSettings guild = Settings.getGuildSettings(g.getId());

            guild.set("xp.levelup", !guild.getBoolean("xp.levelup", true));

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Toggled Server Level Up Messages")
                    .addField(":1234: Level Up Messages", guild.getBoolean("xp.levelup", true) ? "Enabled" : "Disabled", true)
                    .setColor(Colors.ACCENT);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } catch (Exception e) {
            interaction.reply("An error occurred while toggling level up messages.").setEphemeral(true).queue();
        }
    }
}
