package flustix.fluxifyed.commands.auth;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class GenerateTokenSlashCommand extends SlashCommand {
    public GenerateTokenSlashCommand() {
        super("gentoken", "Generate a token for the API");
    }

    public void execute(SlashCommandInteraction interaction) {
        String token = "";

        for (int i = 0; i < 32; i++)
            token += genRandomChar();

        try {
            Database.executeQuery("INSERT INTO tokens (token, userid) VALUES ('" + token + "', '" + interaction.getUser().getId() + "') ON DUPLICATE KEY UPDATE token = '" + token + "'");
            SlashCommandUtils.replyEphemeral(interaction, "Your token is: `" + token + "`");
        } catch (Exception e) {
            e.printStackTrace();
            SlashCommandUtils.replyEphemeral(interaction, "An error occurred while generating your token");
        }
    }

    String genRandomChar() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        return chars.charAt((int) (Math.random() * chars.length())) + "";
    }
}
