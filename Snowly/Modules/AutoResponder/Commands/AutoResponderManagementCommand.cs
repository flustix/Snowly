using DSharpPlus.Entities;
using Snowly.Commands;

namespace Snowly.Modules.AutoResponder.Commands;

public class AutoResponderManagementCommand : ISlashCommandGroup
{
    public string Name => "auto-responder";
    public string Description => "Manage auto-responses.";
    public DiscordPermissions? Permission => DiscordPermissions.ManageMessages;
    public bool AllowInDM => false;

    public IEnumerable<ISlashCommand> Subcommands => new List<ISlashCommand>
    {
        new AutoResponderAddCommand(),
        new AutoResponderRemoveCommand()
    };
}
