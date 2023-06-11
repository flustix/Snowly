package flustix.fluxifyed.modules.economy.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.economy.EconomyModule;
import flustix.fluxifyed.modules.economy.components.EcoGuild;
import flustix.fluxifyed.modules.economy.components.EcoUser;
import flustix.fluxifyed.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class DailySlashCommand extends SlashCommand {
    public DailySlashCommand() {
        super("daily", "Get your daily reward.", true);
    }

    public void execute(SlashCommandInteraction interaction) {
        Guild guild = interaction.getGuild();
        if (guild == null) return;

        EcoGuild ecoGuild = EconomyModule.getGuild(guild.getId());
        EcoUser ecoUser = ecoGuild.getUser(interaction.getUser().getId());

        if (ecoUser.canDaily()) {
            int reward = ecoUser.claimDaily();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Daily reward")
                    .setDescription("You claimed your daily reward of **" + reward + ":coin: (" + ecoUser.getDailyStreak() + "x streak)** coins!")
                    .setColor(Colors.SUCCESS);

            interaction.replyEmbeds(embed.build()).queue();
        } else {
            long timeLeft = ecoUser.nextDaily() - System.currentTimeMillis();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Daily reward")
                    .setDescription("You already claimed your daily reward today!")
                    .setFooter("You can claim it again in " + FormatUtils.timeToString(timeLeft))
                    .setColor(Colors.ERROR);

            interaction.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
