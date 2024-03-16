using Snowly.Commands;
using Snowly.Modules.XP.Commands.Management.LevelRoles;

namespace Snowly.Modules.XP.Commands.Management;

public class LevelRolesCommand : ISlashCommandGroup
{
    public string Name => "level-roles";
    public string Description => "Manage level roles";
    public bool AllowInDM => false;
    public int Depth => 2;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[]
    {
        new LevelRolesListCommand(),
        new LevelRolesAddCommand(),
        new LevelRolesRemoveCommand()
    };
}
