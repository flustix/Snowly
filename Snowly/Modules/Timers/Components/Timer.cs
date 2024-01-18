using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Modules.Timers.Components;

public class Timer
{
    [BsonId]
    public Guid ID { get; set; } = Guid.NewGuid();

    [BsonElement("GuildId")]
    public ulong GuildID { get; set; }

    [BsonElement("ChannelId")]
    public ulong ChannelID { get; set; }

    public int Hour { get; set; }
    public int Minute { get; set; }

    public string Message { get; set; }
    public string Random { get; set; }

    public int AntiRepeat { get; set; }
    public string AntiRepeatHistory { get; set; } = string.Empty;
}
