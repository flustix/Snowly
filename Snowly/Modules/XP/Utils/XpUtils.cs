using MongoDB.Driver;
using Snowly.Database;
using Snowly.Modules.XP.Components;

namespace Snowly.Modules.XP.Utils;

public static class XpUtils
{
    #region Collections

    private static IMongoCollection<XpUser> users => MongoDatabase.GetCollection<XpUser>("xp-users");
    private static IMongoCollection<XpRewardRole> rewardroles => MongoDatabase.GetCollection<XpRewardRole>("xp-reward-roles");
    private static IMongoCollection<XpMultiplierRole> multiplierroles => MongoDatabase.GetCollection<XpMultiplierRole>("xp-multiplier-roles");
    private static IMongoCollection<XpChannelMultiplier> multiplierchannels => MongoDatabase.GetCollection<XpChannelMultiplier>("xp-multiplier-channels");

    private static List<XpUser> allUsers => users.Find(x => true).ToList();
    private static List<XpRewardRole> allRewardRoles => rewardroles.Find(x => true).ToList();
    private static List<XpMultiplierRole> allMultiplierRoles => multiplierroles.Find(x => true).ToList();
    private static List<XpChannelMultiplier> allMultiplierChannels => multiplierchannels.Find(x => true).ToList();

    #endregion

    #region Users

    public static XpUser GetUser(ulong guildid, ulong userid)
    {
        var user = allUsers.FirstOrDefault(x => x.GuildId == guildid && x.UserId == userid);
        if (user != null) return user;

        var newUser = new XpUser
        {
            GuildId = guildid,
            UserId = userid
        };

        users.InsertOne(newUser);
        return newUser;
    }

    public static void UpdateUser(XpUser user) => users.ReplaceOne(x => x.Id == user.Id, user);

    public static List<XpUser> GetTopUsers(ulong guildid) => allUsers.Where(x => x.GuildId == guildid).OrderByDescending(x => x.Xp).ToList();
    public static List<XpUser> GetTopUsers(ulong guildid, int count) => GetTopUsers(guildid).Take(count).ToList();

    public static int GetRank(ulong guildid, ulong userid)
    {
        var top = GetTopUsers(guildid);
        var user = top.FirstOrDefault(x => x.UserId == userid);
        return top.IndexOf(user) + 1;
    }

    #endregion

    #region Reward Roles

    public static List<XpRewardRole> GetRewardRoles(ulong guildid) => allRewardRoles.Where(x => x.GuildId == guildid).ToList();

    public static void AddRewardRole(ulong guildid, long level, ulong roleid)
    {
        var newRole = new XpRewardRole
        {
            GuildId = guildid,
            Level = level,
            RoleId = roleid
        };

        rewardroles.InsertOne(newRole);
    }

    public static void RemoveRewardRole(ulong guildId, ulong roleId)
    {
        rewardroles.DeleteOne(x => x.GuildId == guildId && x.RoleId == roleId);
    }

    #endregion

    #region Multipliers

    public static List<XpMultiplierRole> GetMultiplierRoles(ulong guildid) => allMultiplierRoles.Where(x => x.GuildId == guildid).ToList();
    public static List<XpChannelMultiplier> GetMultiplierChannels(ulong guildid) => allMultiplierChannels.Where(x => x.GuildId == guildid).ToList();

    #endregion

    public static long GetLevel(long xp) => (long)Math.Sqrt(xp / 100f);
    public static long GetXpForLevel(long level) => (long)Math.Pow(level, 2) * 100;
}
