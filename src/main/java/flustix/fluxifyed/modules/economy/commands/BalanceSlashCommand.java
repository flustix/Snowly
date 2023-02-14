package flustix.fluxifyed.modules.economy.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.economy.EconomyModule;
import flustix.fluxifyed.modules.economy.components.EcoGuild;
import flustix.fluxifyed.modules.economy.components.EcoUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class BalanceSlashCommand extends SlashCommand {
    public BalanceSlashCommand() {
        super("balance", "Get your balance.", true);
    }

    public void execute(SlashCommandInteraction interaction) {
        Guild guild = interaction.getGuild();
        if (guild == null) return;

        EcoGuild ecoGuild = EconomyModule.getGuild(guild.getId());
        EcoUser ecoUser = ecoGuild.getUser(interaction.getUser().getId());

        String name = interaction.getUser().getName();
        if (interaction.getMember() != null) name = interaction.getMember().getEffectiveName();

        if (name.endsWith("s") || name.endsWith("x")) name += "'";
        else name += "'s";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(name + " balance")
                .addField(":coin: Coins", String.valueOf(ecoUser.getBalance()), true)
                .addField("Daily Streak", String.valueOf(ecoUser.getDailyStreak()), true)
                .setColor(Colors.ACCENT);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
