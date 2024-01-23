using DSharpPlus;
using Snowly.Commands;

namespace Snowly.Modules.AutoResponder.Commands;

public class AutoResponderManagementCommand : ISlashCommandGroup
{
    public string Name => "auto-responder";
    public string Description => "Manage auto-responses.";
    public Permissions? Permission => Permissions.ManageMessages;

    public IEnumerable<ISlashCommand> Subcommands => new List<ISlashCommand>
    {
        new AutoResponderAddCommand(),
        new AutoResponderRemoveCommand()
    };
}
