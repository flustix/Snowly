namespace Fluxifyed.CC.Commands; 

public class StopConsoleCommand : IConsoleCommand {
    public string Name => "stop";
    public string Usage => "stop";
    public string Description => "Stops the bot";
    
    public void Execute(string[] args) {
        Console.Write("Stopping bot...");
        ConsoleCommands.Stop();
        Environment.Exit(0);
    }
}