package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class PingSlashCommand extends SlashCommand {
    public PingSlashCommand() {
        super("ping", "Pong!");
    }

    public void execute(SlashCommandInteraction interaction) {
        SlashCommandUtils.reply(interaction, "<a:catItSlaps:935449425722109993>", (hook) -> {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Main.accentColor)
                    .addField(":1234: Ping", interaction.getJDA().getGatewayPing() + "ms", true)
                    .addField(":1234: Rest", interaction.getJDA().getRestPing().complete() + "ms", true);

            hook.editOriginal(
                    new MessageEditBuilder()
                            .setContent("")
                            .setEmbeds(embed.build())
                            .build()
            ).complete();
        });
    }
}
