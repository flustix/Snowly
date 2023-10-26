using MongoDB.Bson;
using Realms;

namespace Fluxifyed.Modules.XP.Components;

public class XpRewardRole {
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong RoleId { get; set; }
    public long Level { get; set; }
}
