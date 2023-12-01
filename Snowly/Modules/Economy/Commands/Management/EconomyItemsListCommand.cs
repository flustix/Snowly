using DSharpPlus.Entities;
using Snowly.Commands;

namespace Snowly.Modules.Economy.Commands.Management;

public class EconomyItemsListCommand : ISlashCommand
{
    public string Name => "list";
    public string Description => "List all items in the shop.";

    public void Handle(DiscordInteraction interaction)
    {
    }
}
