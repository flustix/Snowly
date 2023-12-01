using MongoDB.Driver;
using Snowly.Database;
using Snowly.Modules.Economy.Components;

namespace Snowly.Modules.Economy.Utils;

public static class ShopItemUtils
{
    private static IMongoCollection<ShopItem> items => MongoDatabase.GetCollection<ShopItem>("shop-items");
    private static List<ShopItem> allItems => items.Find(u => true).ToList();

    public static void Add(ShopItem item) => items.InsertOne(item);
    public static void Update(ShopItem item) => items.ReplaceOne(i => i.Id == item.Id, item);
    public static void Remove(ShopItem item) => items.DeleteOne(i => i.Id == item.Id);

    public static ShopItem Get(string id) => Get(Guid.Parse(id));
    public static ShopItem Get(Guid id) => allItems.FirstOrDefault(x => x.Id == id);

    public static List<ShopItem> GetAllFromGuild(ulong guildid) => allItems.Where(x => x.GuildId == guildid).ToList();
}
