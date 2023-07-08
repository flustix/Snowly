using Fluxifyed.Logging;

namespace Fluxifyed.CC.Commands; 

public class SayConsoleCommand : MessageBaseConsoleCommand {
    public override string Name => "say";
    public override string Usage => "say <channel id> <message>";
    public override string Description => "Sends a message to a channel";
    
    public override void Execute(string[] args) {
        if (args.Length < 2) {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.Write("Not enough arguments!\n");
            Logger.EmptyLine();
            return;
        }
        
        SendMessage(args);
    }
}