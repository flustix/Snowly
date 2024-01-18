using MongoDB.Driver;
using Snowly.Database;

namespace Snowly.Config;

public static class Configs
{
    private static IMongoCollection<UserConfig> users => MongoDatabase.GetCollection<UserConfig>("user-configs");
    private static IMongoCollection<GuildConfig> guilds => MongoDatabase.GetCollection<GuildConfig>("guild-configs");

    private static List<UserConfig> allUsers => users.Find(x => true).ToList();
    private static List<GuildConfig> allGuilds => guilds.Find(x => true).ToList();

    public static UserConfig GetUserConfig(ulong userId)
    {
        var user = allUsers.FirstOrDefault(x => x.ID == userId);
        if (user != null) return user;

        users.InsertOne(user = new UserConfig { ID = userId });
        return user;
    }

    public static GuildConfig GetGuildConfig(ulong guildId)
    {
        var guild = allGuilds.FirstOrDefault(x => x.ID == guildId);
        if (guild != null) return guild;

        guilds.InsertOne(guild = new GuildConfig { ID = guildId });
        return guild;
    }

    public static void UpdateUserConfig(UserConfig user) => users.ReplaceOne(x => x.ID == user.ID, user);
    public static void UpdateGuildConfig(GuildConfig guild) => guilds.ReplaceOne(x => x.ID == guild.ID, guild);
}
