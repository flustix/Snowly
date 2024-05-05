using DSharpPlus.Entities;
using Snowly.Commands;

namespace Snowly.Modules.Economy.Commands.Management;

public class EconomyCommand : ISlashCommandGroup
{
    public string Name => "economy";
    public string Description => "Manage the economy of your server.";
    public DiscordPermissions? Permission => DiscordPermissions.ManageGuild;
    public bool AllowInDM => false;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[]
    {
        new EconomyConfigCommand(),
        new EconomyItemsCommand()
    };
}
