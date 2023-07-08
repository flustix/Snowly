using Fluxifyed.Modules.Economy.Components;
using Realms;

namespace Fluxifyed.Modules.Economy.Utils; 

public static class EcoUtils {
    public static EconomyUser GetUser(Realm realm, string guildid, string userid) {
        var user = realm.All<EconomyUser>().FirstOrDefault(x => x.GuildId == guildid && x.UserId == userid);

        if (user != null) return user;
        
        realm.Add(user = new EconomyUser {
            GuildId = guildid,
            UserId = userid
        });

        return user;
    }
}