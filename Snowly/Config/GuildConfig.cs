using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Config;

public class GuildConfig
{
    [BsonId]
    public ulong ID { get; init; }

    // General
    [BsonElement("LoggingChannelId")]
    public ulong LoggingChannelID { get; set; }

    // XP
    [BsonElement("XpEnabled")]
    public bool XPEnabled { get; set; } = true;

    public bool LevelUpMessages { get; set; } = true;

    [BsonElement("LevelUpChannelId")]
    public ulong LevelUpChannelID { get; set; }

    // Economy
    public string CurrencyName { get; set; } = "coins";
    public string CurrencySymbol { get; set; } = ":coin:";
}
