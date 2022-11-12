package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ToggleXPSlashCommand extends SlashCommand {
    public ToggleXPSlashCommand() {
        super("togglexp", "Toggle XP on the server.", true);
        addPermissions(Permission.MANAGE_SERVER);
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

            interaction.replyEmbeds(embed.build()).queue();
        } catch (Exception e) {
            interaction.reply("An error occurred while toggling xp.").setEphemeral(true).queue();
        }
    }
}
