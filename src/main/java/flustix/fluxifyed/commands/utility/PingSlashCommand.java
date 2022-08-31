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
        long time = System.currentTimeMillis();
        SlashCommandUtils.reply(interaction, "<a:catItSlaps:935449425722109993>", (hook) -> {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Pong!")
                    .setColor(Main.accentColor)
                    .addField("\ud83d\udcf6 Ping", (System.currentTimeMillis() - time) + "ms", true);

            hook.editOriginal(
                    new MessageBuilder()
                            .setEmbeds(embed.build())
                            .build()
            ).complete();
        });
    }
}
