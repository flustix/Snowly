package flustix.fluxifyed.modules.fun.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CoinflipSlashCommand extends SlashCommand {
    public CoinflipSlashCommand() {
        super("coinflip", "Flip a coin!");
    }

    public void execute(SlashCommandInteraction interaction) {
        String result = Math.random() < 0.5 ? "Heads" : "Tails";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Coinflip")
                .setColor(Main.accentColor)
                .setDescription("The coin landed on **" + result + "**!");

        interaction.replyEmbeds(embed.build()).queue();
    }
}
