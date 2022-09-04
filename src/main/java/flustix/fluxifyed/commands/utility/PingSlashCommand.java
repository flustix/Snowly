package flustix.fluxifyed.commands.utility;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

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
                    .addField(":1234: Rest", interaction.getJDA().getRestPing().complete() + "ms", true)
                    .addField(":hash: Shard ID", interaction.getJDA().getShardInfo().getShardId() + "", true);

            hook.editOriginal(
                    new MessageBuilder()
                            .setEmbeds(embed.build())
                            .build()
            ).complete();
        });
    }
}
