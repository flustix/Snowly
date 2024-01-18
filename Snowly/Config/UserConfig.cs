using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Config;

public class UserConfig
{
    [BsonId]
    public ulong ID { get; init; }

    // XP
    public bool LevelUpMessages { get; set; } = true;
}
