using Fluxifyed.Logging;

namespace Fluxifyed.CC.Commands; 

public class HelpConsoleCommand : IConsoleCommand {
    public string Name => "help";
    public string Usage => "help";
    public string Description => "Lists all commands";
    
    public void Execute(string[] args) {
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.Write("Available commands:\n");
        Console.ForegroundColor = ConsoleColor.White;
        
        foreach (var command in ConsoleCommands.Commands) {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.Write($"{command.Name} ");
            Console.ForegroundColor = ConsoleColor.White;
            Console.Write($"- {command.Description}\n");
        }
        
        Logger.EmptyLine();
    }
}