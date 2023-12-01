using Snowly.Commands;
using Snowly.Modules.XP.Commands.Management.Multipliers;

namespace Snowly.Modules.XP.Commands.Management;

public class MultipliersCommand : ISlashCommandGroup {
    public string Name => "multipliers";
    public string Description => "Manage role and channel multipliers.";

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[] {
        new RoleMultipliersListCommand()
    };
}
