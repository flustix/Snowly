using Realms;

namespace Fluxifyed.Modules.XP.Components; 

public class XpRewardRole : RealmObject {
    [Indexed] public string GuildId { get; set; }
    [Indexed] public string RoleId { get; set; }
    public long Level { get; set; }
}