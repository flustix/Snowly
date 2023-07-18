using Fluxifyed.Modules.XP.Utils;
using Realms;

namespace Fluxifyed.Modules.XP.Components;

public class XpUser : RealmObject {
    [Indexed] public string GuildId { get; set; }
    [Indexed] public string UserId { get; set; }
    public long Xp { get; set; }
    public long LastMessage { get; set; }

    [Ignored] public long Level => XpUtils.GetLevel(Xp);
    [Ignored] public long TotalXpForNextLevel => XpUtils.GetXpForLevel(Level + 1);
    [Ignored] public long TotalXpForCurrentLevel => XpUtils.GetXpForLevel(Level);
    [Ignored] public long XpFromCurrentToNext => TotalXpForNextLevel - TotalXpForCurrentLevel;
    [Ignored] public long XpLeft => TotalXpForNextLevel - Xp;
    [Ignored] public long LevelProgress => XpFromCurrentToNext - XpLeft;
    [Ignored] public double LevelProgressPercent => (double) LevelProgress / XpFromCurrentToNext;
}
