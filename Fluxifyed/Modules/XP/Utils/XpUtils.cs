using Fluxifyed.Modules.XP.Components;
using Realms;

namespace Fluxifyed.Modules.XP.Utils;

public static class XpUtils {
    public static XpUser GetUser(Realm realm, string guildid, string userid) {
        var user = realm.All<XpUser>().FirstOrDefault(x => x.GuildId == guildid && x.UserId == userid);

        if (user != null) return user;
        return realm.Add(new XpUser {
            GuildId = guildid,
            UserId = userid
        });
    }

    public static List<XpUser> GetTopUsers(Realm realm, string guildid) {
        var users = realm.All<XpUser>().Where(x => x.GuildId == guildid).OrderByDescending(x => x.Xp).ToList();
        return users.ToList();
    }

    public static List<XpUser> GetTopUsers(Realm realm, string guildid, int count) {
        return GetTopUsers(realm, guildid).Take(count).ToList();
    }

    public static int GetRank(Realm realm, string guildid, string userid) {
        var users = GetTopUsers(realm, guildid);
        var user = users.FirstOrDefault(x => x.UserId == userid);
        return users.IndexOf(user) + 1;
    }

    public static long GetLevel(long xp) => (long) Math.Sqrt(xp / 100f);
    public static long GetXpForLevel(long level) => (long) Math.Pow(level, 2) * 100;
}
