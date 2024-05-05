using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Modules.XP.Commands.Management;

namespace Snowly.Modules.XP.Commands;

public class XpCommand : ISlashCommandGroup
{
    public string Name => "xp";
    public string Description => "Manage the XP system.";
    public DiscordPermissions? Permission => DiscordPermissions.ManageGuild;
    public bool AllowInDM => false;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[]
    {
        new LevelRolesCommand(),
        new MultipliersCommand(),
        new EnableXpCommand(),
        new ModifyXpCommand(),
        new ToggleServerLevelUpCommand()
    };
}
