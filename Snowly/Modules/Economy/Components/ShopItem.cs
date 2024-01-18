using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Modules.Economy.Components;

public class ShopItem
{
    [BsonId]
    public Guid Id { get; init; } = Guid.NewGuid();

    [BsonElement("GuildID")]
    public ulong GuildID { get; set; }

    public string Name { get; set; } = string.Empty;
    public string Icon { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public int Price { get; set; }
}
