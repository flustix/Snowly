package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorSlashCommand extends SlashCommand {
    public CalculatorSlashCommand() {
        super("calc", "pro calc ulator");
        addOption(OptionType.STRING, "equation", "The math equation to calculate", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping equationMapping = interaction.getOption("equation");
        if (equationMapping == null) return;

        try {
            Expression expression = new ExpressionBuilder(equationMapping.getAsString()).build();
            String resultString = String.valueOf(expression.evaluate());

            if (resultString.endsWith(".0"))
                resultString = resultString.substring(0, resultString.length() - 2);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Calculator")
                    .addField(":asterisk: Equation", equationMapping.getAsString(), false)
                    .addField(":1234: Result", resultString, false)
                    .setColor(Main.accentColor);

            interaction.replyEmbeds(embed.build()).queue();
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Calculator")
                    .addField(":x: Error", "Invalid equation", false)
                    .setColor(0xFF5555);

            interaction.replyEmbeds(embed.build()).queue();
        }
    }
}
