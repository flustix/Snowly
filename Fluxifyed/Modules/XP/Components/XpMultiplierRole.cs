using Realms;

namespace Fluxifyed.Modules.XP.Components; 

public class XpMultiplierRole : RealmObject {
    [Indexed] public string GuildId { get; set; }
    [Indexed] public string RoleId { get; set; }
    public double Multiplier { get; set; }
}