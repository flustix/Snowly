using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Utility.Commands;

public class PingCommand : ISlashCommand {
    public string Name => "ping";
    public string Description => "Pong!";

    public void Handle(DiscordInteraction interaction) {
        interaction.ReplyEmbed(new CustomEmbed {
                Description = "Pong!",
                Color = Colors.Random,
                Footer = new CustomEmbedFooter {
                    Text = $"Latency: {Fluxifyed.Bot.Ping}ms"
                }
            }
        );
    }
}
