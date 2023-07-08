using Realms;

namespace Fluxifyed.Modules.XP.Components; 

public class XpChannelMultiplier : RealmObject {
    [Indexed] public string GuildId { get; set; }
    [Indexed] public string ChannelId { get; set; }
    public double Multiplier { get; set; }
}