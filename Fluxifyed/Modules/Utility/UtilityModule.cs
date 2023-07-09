using Fluxifyed.Commands;
using Fluxifyed.Modules.Utility.Commands;

namespace Fluxifyed.Modules.Utility; 

public class UtilityModule : IModule {
    public string Name => "Utility";
    public string Description => "Utility commands for your server.";
    public List<ISlashCommand> SlashCommands => new() {
        new AboutCommand(),
        new AvatarCommand(),
        new PingCommand(),
        new SayCommand()
    };
}