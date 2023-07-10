using Fluxifyed.Commands;
using Fluxifyed.Modules.Economy.Commands;

namespace Fluxifyed.Modules.Economy; 

public class EconomyModule : IModule {
    public string Name => "Economy";
    public string Description => "A virtual economy for your server members.";
    public List<ISlashCommand> SlashCommands => new() {
        new BalanceCommand(),
        new DailyCommand(),
        new TopBalCommand(),
        new TopStreakCommand()
        // new GiveCommand(),
        // new ModifyCommand(),
        // new ShopCommand(),
        // new TransferCommand()
    };
}