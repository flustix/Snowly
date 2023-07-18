using Fluxifyed.Commands;

namespace Fluxifyed.Modules.Economy.Commands.Management;

public class EconomyCommand : ISlashCommandGroup {
    public string Name => "economy";
    public string Description => "Manage the economy of your server.";

    public ISlashCommand[] Subcommands => new ISlashCommand[] {
        new EconomyConfigCommand()
    };
}
