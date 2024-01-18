using MongoDB.Bson;

namespace Snowly.Modules.XP.Components;

public class XpMultiplierRole
{
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong RoleId { get; set; }
    public double Multiplier { get; set; }
}
