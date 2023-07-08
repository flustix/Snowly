using DSharpPlus;
using Fluxifyed.Commands;
using Fluxifyed.Modules.XP.Commands.Management;

namespace Fluxifyed.Modules.XP.Commands; 

public class XpCommand : ISlashCommandGroup {
    public string Name => "xp";
    public string Description => "Manage the XP system.";
    public Permissions? Permission => Permissions.ManageGuild;

    public ISlashCommand[] Subcommands => new ISlashCommand[] {
        new LevelRolesCommand(),
        new MultipliersCommand(),
        new EnableXpCommand(),
        new ModifyXpCommand(),
        new ToggleServerLevelUpCommand()
    };
}