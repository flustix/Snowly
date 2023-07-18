using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Fun.Commands;

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
