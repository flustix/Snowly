using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Modules.Economy.Components;

public class EconomyUser
{
    [BsonId]
    public ObjectId ID { get; set; } = ObjectId.GenerateNewId();

    [BsonElement("GuildId")]
    public ulong GuildID { get; set; }

    [BsonElement("UserId")]
    public ulong UserID { get; set; }

    public long Balance { get; set; }
    public int DailyStreak { get; set; }
    public DateTimeOffset LastDaily { get; set; }

    [BsonIgnore]
    public bool CanDaily => DateTimeOffset.UtcNow >= LastDaily.AddHours(20);

    [BsonIgnore]
    public bool StreakLost => DateTimeOffset.UtcNow >= LastDaily.AddHours(40);

    [BsonIgnore]
    public int ActualStreak => StreakLost ? 0 : DailyStreak;

    [BsonIgnore]
    public long TimeUntilDaily => (LastDaily.AddHours(20) - DateTimeOffset.UtcNow).Ticks / TimeSpan.TicksPerMillisecond;
}
