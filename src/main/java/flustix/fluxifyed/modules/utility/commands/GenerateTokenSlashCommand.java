package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.v1.authentification.TokenGen;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class GenerateTokenSlashCommand extends SlashCommand {
    public GenerateTokenSlashCommand() {
        super("gentoken", "Generate a token for the API");
    }

    public void execute(SlashCommandInteraction interaction) {
        String token = TokenGen.generateToken(interaction.getUser().getId());

        if (!token.isEmpty()) {
            SlashCommandUtils.replyEphemeral(interaction, "Your token is: `" + token + "`");
        } else {
            SlashCommandUtils.replyEphemeral(interaction, "An error occurred while generating your token");
        }
    }
}
