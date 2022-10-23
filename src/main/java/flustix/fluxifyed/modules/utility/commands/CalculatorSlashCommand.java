package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
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
            Expression expression = new ExpressionBuilder(equationMapping.getAsString()).variable("amongus").build();
            expression.setVariable("amongus", 69);

            double result = expression.evaluate();

            String resultString = String.valueOf(result);

            if (resultString.endsWith(".0")) {
                resultString = resultString.substring(0, resultString.length() - 2);
            }

            interaction.reply(resultString).queue();
        } catch (Exception e) {
            interaction.reply("Invalid equation!").queue();
        }
    }
}
