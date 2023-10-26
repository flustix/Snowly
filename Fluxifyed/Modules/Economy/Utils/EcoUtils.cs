using Fluxifyed.Database;
using Fluxifyed.Modules.Economy.Components;
using MongoDB.Driver;
using Realms;

namespace Fluxifyed.Modules.Economy.Utils;

public static class EcoUtils {
    private static IMongoCollection<EconomyUser> users => MongoDatabase.GetCollection<EconomyUser>("economy-users");
    private static List<EconomyUser> allUsers => users.Find(u => true).ToList();

    public static EconomyUser GetUser(ulong guildid, ulong userid) {
        var user = allUsers.FirstOrDefault(x => x.GuildId == guildid && x.UserId == userid);
        if (user != null) return user;

        users.InsertOne(user = new EconomyUser {
            GuildId = guildid,
            UserId = userid
        });

        return user;
    }

    public static List<EconomyUser> GetTopBalUsers(ulong guildid) => allUsers.Where(x => x.GuildId == guildid).OrderByDescending(x => x.Balance).ToList();
    public static List<EconomyUser> GetTopBalUsers(ulong guildid, int count) => GetTopBalUsers(guildid).Take(count).ToList();

    public static List<EconomyUser> GetTopStreakUsers(ulong guildid) {
        var top = allUsers.Where(x => x.GuildId == guildid).ToList();
        return top.OrderByDescending(x => x.ActualStreak).Where(x => x.ActualStreak > 0).ToList();
    }

    public static List<EconomyUser> GetTopStreakUsers(ulong guildid, int count) =>
        GetTopStreakUsers(guildid).Take(count).ToList();

    public static void UpdateUser(EconomyUser user) => users.ReplaceOne(u => u.Id == user.Id, user);
}
