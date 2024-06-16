using Microsoft.Extensions.Logging;
using MongoDB.Driver;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Database;
using Timer = Snowly.Modules.Timers.Components.Timer;

namespace Snowly.Modules.Timers;

public class TimersModule : IModule
{
    public string Name => "Timers";
    public string Description => "Send custom messages at a set time.";
    public List<ISlashCommand> SlashCommands => new();

    private int minute;

    public TimersModule()
    {
        // ReSharper disable once FunctionNeverReturns
        Task.Run(() =>
        {
            while (true)
            {
                var now = DateTime.Now;

                if (now.Minute == minute)
                {
                    Thread.Sleep(1000);
                    continue;
                }

                minute = now.Minute;

                runTimers();
            }
        });
    }

    private static void runTimers()
    {
        var now = DateTime.Now;
        var collection = MongoDatabase.GetCollection<Timer>("timers");
        var timers = collection.Find(timer => timer.Hour == now.Hour && timer.Minute == now.Minute).ToList();

        foreach (var timer in timers)
        {
            try
            {
                var guild = Snowly.Bot.Guilds[timer.GuildID];
                var channel = guild.Channels[timer.ChannelID];

                var message = timer.Message;
                var random = JsonConvert.DeserializeObject(timer.Random);

                if (random is JArray randomList)
                {
                    var randomIndex = new Random().Next(0, randomList.Count);

                    var historySplit = timer.AntiRepeatHistory?.Split(",").ToList() ?? new List<string>();
                    if (timer.AntiRepeat == historySplit.Count) historySplit.RemoveAt(0);

                    while (historySplit.Contains(randomIndex.ToString()) && randomList.Count > 0)
                    {
                        randomIndex = new Random().Next(0, randomList.Count);
                    }

                    historySplit.Add(randomIndex.ToString());
                    timer.AntiRepeatHistory = string.Join(",", historySplit);
                    collection.ReplaceOne(t => t.ID == timer.ID, timer);

                    var randomItem = randomList.ElementAt(randomIndex);

                    foreach (var prop in randomItem.ToObject<Dictionary<string, string>>())
                    {
                        var key = prop.Key;
                        var value = prop.Value;
                        value = value?.Replace("\"", "\\\"");

                        message = message.Replace("{" + key + "}", value);
                    }
                }

                var parsed = JsonConvert.DeserializeObject<CustomMessage>(message);
                channel?.SendMessageAsync(parsed.Content, parsed.ToEmbed());
            }
            catch (Exception e)
            {
                Snowly.Logger.LogError(e, $"Failed to send timer message for {timer.GuildID} in {timer.ChannelID}");
            }
        }
    }
}
