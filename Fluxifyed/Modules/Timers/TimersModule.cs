using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Database;
using Fluxifyed.Logging;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Timer = Fluxifyed.Modules.Timers.Components.Timer;

namespace Fluxifyed.Modules.Timers; 

public class TimersModule : IModule {
    public string Name => "Timers";
    public string Description => "Send custom messages at a set time.";
    public List<ISlashCommand> SlashCommands => new();
    
    private int _minute;
    
    public TimersModule() {
        // ReSharper disable once FunctionNeverReturns
        Task.Run(() => {
            while (true) {
                var now = DateTime.Now;

                if (now.Minute == _minute) {
                    Thread.Sleep(1000);
                    continue;
                }

                _minute = now.Minute;
                
                RunTimers();
            }
        });
    }

    private static void RunTimers() {
        RealmAccess.Run(realm => {
            var now = DateTime.Now;
            var timers = realm.All<Timer>().Where(timer => timer.Hour == now.Hour && timer.Minute == now.Minute);

            foreach (var timer in timers) {
                try {
                    var guild = Fluxifyed.Bot.Guilds[ulong.Parse(timer.GuildId)];
                    var channel = guild.Channels[ulong.Parse(timer.ChannelId)];

                    var message = timer.Message;
                    var random = JsonConvert.DeserializeObject(timer.Random);
                    
                    if (random is JArray randomList) {
                        var randomIndex = new Random().Next(0, randomList.Count);
                        
                        var historySplit = timer.AntiRepeatHistory?.Split(",").ToList() ?? new List<string>();
                        if (timer.AntiRepeat == historySplit.Count) historySplit.RemoveAt(0);
                        
                        while (historySplit.Contains(randomIndex.ToString()) && randomList.Count > 0) {
                            randomIndex = new Random().Next(0, randomList.Count);
                        }

                        historySplit.Add(randomIndex.ToString());
                        timer.AntiRepeatHistory = string.Join(",", historySplit);

                        var randomItem = randomList.ElementAt(randomIndex);
                        
                        foreach (var prop in randomItem.ToObject<Dictionary<string, string>>()) {
                            var key = prop.Key;
                            var value = prop.Value;
                            value = value?.Replace("\"", "\\\"");
                            
                            message = message.Replace("{" + key + "}", value);
                        }
                    }
                    
                    var parsed = JsonConvert.DeserializeObject<CustomMessage>(message);
                    channel?.SendMessageAsync(parsed.Content, parsed.ToEmbed());
                } catch (Exception e) {
                    Fluxifyed.Logger.LogError(e, $"Failed to send timer message for {timer.GuildId} in {timer.ChannelId}");
                }
            }
        });
    }
}