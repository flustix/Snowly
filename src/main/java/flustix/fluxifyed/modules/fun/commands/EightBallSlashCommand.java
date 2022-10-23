package flustix.fluxifyed.modules.fun.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class EightBallSlashCommand extends SlashCommand {
    // https://en.wikipedia.org/wiki/Magic_8_Ball
    private static final String[] answers = {
            ":white_check_mark:|It is certain.",
            ":white_check_mark:|It is decidedly so.",
            ":white_check_mark:|Without a doubt.",
            ":white_check_mark:|Yes - definitely.",
            ":white_check_mark:|You may rely on it.",
            ":white_check_mark:|As I see it, yes.",
            ":white_check_mark:|Most likely.",
            ":white_check_mark:|Outlook good.",
            ":white_check_mark:|Yes.",
            ":white_check_mark:|Signs point to yes.",
            ":heavy_minus_sign:|Reply hazy, try again.",
            ":heavy_minus_sign:|Ask again later.",
            ":heavy_minus_sign:|Better not tell you now.",
            ":heavy_minus_sign:|Cannot predict now.",
            ":heavy_minus_sign:|Concentrate and ask again.",
            ":x:|Don't count on it.",
            ":x:|My reply is no.",
            ":x:|My sources say no.",
            ":x:|Outlook not so good.",
            ":x:|Very doubtful."
    };

    public EightBallSlashCommand() {
        super("8ball", "Ask the magic 8ball a question!");
        addOption(OptionType.STRING, "question", "The question you want to ask the magic 8ball.", true, false);
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        OptionMapping question = interaction.getOption("question");
        if (question == null) return;

        String selection = answers[(int) (Math.random() * answers.length)];
        String[] split = selection.split("\\|");
        String emoji = split[0];
        String answer = split[1];

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Magic 8ball")
                .setColor(Main.accentColor)
                .addField(":question: Question", question.getAsString(), false)
                .addField(emoji + " Answer", answer, false);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
