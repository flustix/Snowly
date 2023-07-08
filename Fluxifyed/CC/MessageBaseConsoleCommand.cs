using DSharpPlus;
using Fluxifyed.Logging;

namespace Fluxifyed.CC; 

public abstract class MessageBaseConsoleCommand : IConsoleCommand {
    public abstract string Name { get; }
    public abstract string Usage { get; }
    public abstract string Description { get; }

    public abstract void Execute(string[] args);
    
    protected async void SendMessage(string[] args, bool reply = false) {
        if (await Fluxifyed.Bot.GetChannelAsync(ulong.Parse(args[0])) is not { Type: ChannelType.Text } channel) {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.Write("Channel not found!\n");
            Logger.EmptyLine();
        } else {
            var replyId = reply ? ulong.Parse(args[1]) : 0;
            var message = string.Join(" ", args.Skip(reply ? 2 : 1));
            
            if (reply && replyId != 0) {
                /*var replyMessage = new MessageReference(replyId, channel.Id, channel.GuildId, true);

                var msg = new DiscordMessageBuilder {
                    
                };
                await channel.SendMessageAsync(msg);*/
                
                throw new NotImplementedException();

                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.Write($"Replied to message in #{channel.Name}!\n");
                Logger.EmptyLine();
            } else {
                await channel.SendMessageAsync(message);
                        
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.Write($"Message sent in #{channel.Name}!\n");
                Logger.EmptyLine();
            }
        }
    }
}