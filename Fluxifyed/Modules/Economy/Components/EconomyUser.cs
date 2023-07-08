using Realms;

namespace Fluxifyed.Modules.Economy.Components; 

public class EconomyUser : RealmObject {
    [Indexed] public string GuildId { get; set; }
    [Indexed] public string UserId { get; set; }
    public long Balance { get; set; }
    public int DailyStreak { get; set; }
    public DateTimeOffset LastDaily { get; set; }
    
    [Ignored] public bool CanDaily => DateTimeOffset.UtcNow >= LastDaily.AddHours(20);
    [Ignored] public bool StreakLost => DateTimeOffset.UtcNow >= LastDaily.AddHours(40);
    [Ignored] public int ActualStreak => StreakLost ? 0 : DailyStreak;
    [Ignored] public long TimeUntilDaily => (LastDaily.AddHours(20) - DateTimeOffset.UtcNow).Ticks / TimeSpan.TicksPerMillisecond;
}