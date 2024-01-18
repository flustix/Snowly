using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Fun.Commands;

public class EightBallCommand : IOptionSlashCommand
{
    public string Name => "8ball";
    public string Description => "Ask the magic 8ball a question.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "question",
            Description = "The question you want to ask the 8ball.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        }
    };

    private static readonly string[] answers =
    {
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

    public void Handle(DiscordInteraction interaction)
    {
        var question = interaction.GetString("question");
        if (question == null) return;

        var answer = answers[new Random().Next(0, answers.Length)];
        var split = answer.Split('|');

        interaction.ReplyEmbed(new CustomEmbed
        {
            Title = "Magic 8ball",
            Fields = new List<CustomEmbedField>
            {
                new()
                {
                    Name = ":question: Question",
                    Value = question,
                    Inline = false
                },
                new()
                {
                    Name = $"{split[0]} Answer",
                    Value = split[1],
                    Inline = false
                }
            },
            Color = split[0] switch
            {
                ":white_check_mark:" => Colors.Success,
                ":heavy_minus_sign:" => Colors.Warning,
                ":x:" => Colors.Error,
                _ => Colors.Info
            }
        });
    }
}
