package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.utils.time.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class StatsSlashCommand extends SlashCommand {
    public StatsSlashCommand() {
        super("stats", "Shows the stats of the bot.");
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Stats")
                .setColor(Main.accentColor)
                .setDescription("Here are the stats of the bot.")
                .addField(":1234: Guilds", String.valueOf(interaction.getJDA().getGuilds().size()), true)
                .addField(":clock1: Uptime", TimeUtils.format(System.currentTimeMillis() - Main.getStartTime()), true);

        interaction.replyEmbeds(embedBuilder.build()).queue();
    }
}
