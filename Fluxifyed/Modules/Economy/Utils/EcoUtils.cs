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
    
    public static List<EconomyUser> GetTopBalUsers(Realm realm, string guildid) => 
        realm.All<EconomyUser>().Where(x => x.GuildId == guildid).OrderByDescending(x => x.Balance).ToList();

    public static List<EconomyUser> GetTopBalUsers(Realm realm, string guildid, int count) =>
        GetTopBalUsers(realm, guildid).Take(count).ToList();
    
    public static List<EconomyUser> GetTopStreakUsers(Realm realm, string guildid) {
        var users = realm.All<EconomyUser>().Where(x => x.GuildId == guildid).ToList();
        return users.OrderByDescending(x => x.ActualStreak).Where(x => x.ActualStreak > 0).ToList();
    }

    public static List<EconomyUser> GetTopStreakUsers(Realm realm, string guildid, int count) =>
        GetTopStreakUsers(realm, guildid).Take(count).ToList();
}