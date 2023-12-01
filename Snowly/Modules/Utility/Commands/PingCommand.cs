using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class PingCommand : ISlashCommand {
    public string Name => "ping";
    public string Description => "Pong!";

    public void Handle(DiscordInteraction interaction) {
        interaction.ReplyEmbed(new CustomEmbed {
                Description = "Pong!",
                Color = Colors.Random,
                Footer = new CustomEmbedFooter {
                    Text = $"Latency: {Snowly.Bot.Ping}ms"
                }
            }
        );
    }
}
