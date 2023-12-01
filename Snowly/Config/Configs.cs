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
        var user = allUsers.FirstOrDefault(x => x.Id == userId);
        if (user != null) return user;

        users.InsertOne(user = new UserConfig { Id = userId });
        return user;
    }

    public static GuildConfig GetGuildConfig(ulong guildId)
    {
        var guild = allGuilds.FirstOrDefault(x => x.Id == guildId);
        if (guild != null) return guild;

        guilds.InsertOne(guild = new GuildConfig { Id = guildId });
        return guild;
    }

    public static void UpdateUserConfig(UserConfig user) => users.ReplaceOne(x => x.Id == user.Id, user);
    public static void UpdateGuildConfig(GuildConfig guild) => guilds.ReplaceOne(x => x.Id == guild.Id, guild);
}
