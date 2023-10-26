using MongoDB.Bson;

namespace Fluxifyed.Modules.XP.Components;

public class XpChannelMultiplier {
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong ChannelId { get; set; }
    public double Multiplier { get; set; }
}
