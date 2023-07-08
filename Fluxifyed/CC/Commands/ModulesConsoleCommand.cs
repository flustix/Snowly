using Fluxifyed.Logging;

namespace Fluxifyed.CC.Commands; 

public class ModulesConsoleCommand : IConsoleCommand {
    public string Name => "modules";
    public string Usage => "modules";
    public string Description => "Lists all modules.";
    public void Execute(string[] args) {
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.Write("Loaded Modules:\n");
        Console.ForegroundColor = ConsoleColor.White;
        
        foreach (var module in Fluxifyed.Modules) {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.Write($"{module.Name} ");
            Console.ForegroundColor = ConsoleColor.White;
            Console.Write($"- {module.Description}\n");
        }
        
        Logger.EmptyLine();
    }
}