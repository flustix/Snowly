using DSharpPlus.EventArgs;
using Midori.Logging;
using MongoDB.Driver;
using Snowly.Commands;
using Snowly.Database;
using Snowly.Modules.AutoResponder.Commands;
using Snowly.Modules.AutoResponder.Components;

namespace Snowly.Modules.AutoResponder;

public class AutoResponderModule : IModule
{
    public string Name => "AutoResponder";
    public string Description => "Automatically responds to messages";

    public List<ISlashCommand> SlashCommands => new()
    {
        new AutoResponderManagementCommand()
    };

    public static IMongoCollection<AutoResponse> Responses => MongoDatabase.GetCollection<AutoResponse>("auto-responses");

    public Task OnMessageReceived(MessageCreateEventArgs args)
    {
        try
        {
            if (args.Author.IsBot)
                return Task.CompletedTask;

            var response = Responses.Find(x => x.GuildID == args.Guild.Id && args.Message.Content.ToLower().Contains(x.Trigger.ToLower())).FirstOrDefault();

            if (response is null)
                return Task.CompletedTask;

            if (response.ChannelID != 0 && response.ChannelID != args.Channel.Id)
                return Task.CompletedTask;

            args.Message.RespondAsync(response.Response);
        }
        catch (Exception e)
        {
            Logger.Error(e, $"Failed to handle auto-response for {args.Message.Content} [{args.Guild.Id}].");
        }

        return Task.CompletedTask;
    }
}
