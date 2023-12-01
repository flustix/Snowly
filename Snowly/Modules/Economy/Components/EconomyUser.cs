using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Modules.Economy.Components;

public class EconomyUser {
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong UserId { get; set; }
    public long Balance { get; set; }
    public int DailyStreak { get; set; }
    public DateTimeOffset LastDaily { get; set; }

    [BsonIgnore] public bool CanDaily => DateTimeOffset.UtcNow >= LastDaily.AddHours(20);
    [BsonIgnore] public bool StreakLost => DateTimeOffset.UtcNow >= LastDaily.AddHours(40);
    [BsonIgnore] public int ActualStreak => StreakLost ? 0 : DailyStreak;
    [BsonIgnore] public long TimeUntilDaily => (LastDaily.AddHours(20) - DateTimeOffset.UtcNow).Ticks / TimeSpan.TicksPerMillisecond;
}
