using DSharpPlus.Entities;
using Snowly.Modules.Utility.Commands.Abstract;

namespace Snowly.Modules.Utility.Commands;

public class SayCommand : AbstractSayCommand {
    public override string Name => "say";
    public override string Description => "Sends a message in a channel.";

    protected override string OptionName => "message";
    protected override string OptionDescription => "The message to send.";

    protected override DiscordMessageBuilder CreateMessage(string content) => new DiscordMessageBuilder().WithContent(content);
}
