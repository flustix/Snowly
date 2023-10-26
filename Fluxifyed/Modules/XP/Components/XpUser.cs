using Fluxifyed.Modules.XP.Utils;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using Realms;

namespace Fluxifyed.Modules.XP.Components;

public class XpUser {
    public ObjectId Id { get; set; } = ObjectId.GenerateNewId();

    public ulong GuildId { get; set; }
    public ulong UserId { get; set; }
    public long Xp { get; set; }
    public long LastMessage { get; set; }

    [BsonIgnore] public long Level => XpUtils.GetLevel(Xp);
    [BsonIgnore] public long TotalXpForNextLevel => XpUtils.GetXpForLevel(Level + 1);
    [BsonIgnore] public long TotalXpForCurrentLevel => XpUtils.GetXpForLevel(Level);
    [BsonIgnore] public long XpFromCurrentToNext => TotalXpForNextLevel - TotalXpForCurrentLevel;
    [BsonIgnore] public long XpLeft => TotalXpForNextLevel - Xp;
    [BsonIgnore] public long LevelProgress => XpFromCurrentToNext - XpLeft;
    [BsonIgnore] public double LevelProgressPercent => (double) LevelProgress / XpFromCurrentToNext;
}
