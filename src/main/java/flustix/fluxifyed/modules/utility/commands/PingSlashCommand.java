package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class PingSlashCommand extends SlashCommand {
    public PingSlashCommand() {
        super("ping", "Pong!");
    }

    public void execute(SlashCommandInteraction interaction) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Pong!")
                .setColor(Colors.ACCENT)
                .addField(":1234: Ping", interaction.getJDA().getGatewayPing() + "ms", true)
                .addField(":1234: Rest", interaction.getJDA().getRestPing().complete() + "ms", true);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
