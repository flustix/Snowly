using Fluxifyed.Commands;
using Fluxifyed.Modules.Fun.Commands;

namespace Fluxifyed.Modules.Fun; 

public class FunModule : IModule {
    public string Name => "Fun";
    public string Description => "Fun commands for everyone!";
    public List<ISlashCommand> SlashCommands => new() {
        new CoinflipCommand(),
        new EightBallCommand()
    };
}