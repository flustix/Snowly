package flustix.fluxifyed.commands.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import flustix.fluxifyed.xp.XP;
import flustix.fluxifyed.xp.types.XPGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ToggleXPSlashCommand extends SlashCommand {
    public ToggleXPSlashCommand() {
        super("togglexp", "Toggle XP on the server.");
        setPermissionLevel(PermissionLevel.ADMIN);
    }

    public void execute(SlashCommandInteraction interaction) {
        try {
            XPGuild guild = XP.getGuild(interaction.getGuild().getId());

            guild.isXpEnabled = !guild.isXpEnabled;

            int enabled = guild.isXpEnabled ? 1 : 0;
            Database.executeQuery("INSERT INTO xpSettings (guildid, enabled) VALUES ('" + interaction.getGuild().getId() + "', " + enabled + ") ON DUPLICATE KEY UPDATE enabled = " + enabled);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Toggled XP")
                    .addField(":1234: XP", guild.isXpEnabled ? "Enabled" : "Disabled", true)
                    .setColor(Main.accentColor);

            SlashCommandUtils.reply(interaction, embed.build());
        } catch (Exception e) {
            SlashCommandUtils.replyEphemeral(interaction, "An error occurred while toggling xp.");
        }
    }
}
