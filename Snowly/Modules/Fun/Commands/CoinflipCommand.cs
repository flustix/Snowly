using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Fun.Commands;

public class CoinflipCommand : ISlashCommand {
    public string Name => "coinflip";
    public string Description => "Flips a coin.";

    public void Handle(DiscordInteraction interaction) {
        string[] options = { "Heads", "Tails" };

        interaction.ReplyEmbed(new CustomEmbed {
            Title = ":coin: Coinflip",
            Description = $"The coin landed on **{options[new Random().Next(0, options.Length)]}**!",
            Color = Colors.Random
        });
    }
}
