using MongoDB.Bson;
using Realms;

namespace Fluxifyed.Modules.Economy.Components;

public class EconomyUser {
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong UserId { get; set; }
    public long Balance { get; set; }
    public int DailyStreak { get; set; }
    public DateTimeOffset LastDaily { get; set; }

    [Ignored] public bool CanDaily => DateTimeOffset.UtcNow >= LastDaily.AddHours(20);
    [Ignored] public bool StreakLost => DateTimeOffset.UtcNow >= LastDaily.AddHours(40);
    [Ignored] public int ActualStreak => StreakLost ? 0 : DailyStreak;
    [Ignored] public long TimeUntilDaily => (LastDaily.AddHours(20) - DateTimeOffset.UtcNow).Ticks / TimeSpan.TicksPerMillisecond;
}
