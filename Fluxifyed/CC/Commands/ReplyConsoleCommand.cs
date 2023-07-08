using Fluxifyed.Logging;

namespace Fluxifyed.CC.Commands; 

public class ReplyConsoleCommand : MessageBaseConsoleCommand {
    public override string Name => "reply";
    public override string Usage => "reply <channel id> <message id> <message>";
    public override string Description => "Replies to a message in a channel";
    
    public override void Execute(string[] args) {
        if (args.Length < 3) {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.Write("Not enough arguments!\n");
            Logger.EmptyLine();
            return;
        }
        
        SendMessage(args, true);
    }
}