using Fluxifyed.Logging;

namespace Fluxifyed.CC.Commands; 

public class GuildsConsoleCommand : IConsoleCommand {
    public string Name => "guilds";
    public string Usage => "guilds";
    public string Description => "Lists all guilds the bot is in";
    
    public void Execute(string[] args) {
        foreach (var guild in Fluxifyed.Bot.Guilds.Values) {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.Write($"{guild.Name} ");
            Console.ForegroundColor = ConsoleColor.White;
            Console.Write($"({guild.Id})\n");
        }

        Logger.EmptyLine();
    }
}