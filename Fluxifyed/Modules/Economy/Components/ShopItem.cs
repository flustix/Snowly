using Realms;

namespace Fluxifyed.Modules.Economy.Components;

public class ShopItem : RealmObject
{
    [PrimaryKey]
    public Guid Id { get; init; } = Guid.NewGuid();

    public string GuildId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Icon { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public int Price { get; set; }

    public static List<ShopItem> GetAll(Realm realm) => realm.All<ShopItem>().ToList();
    public static List<ShopItem> GetAllFromGuild(Realm realm, string guildId) => GetAll(realm).Where(x => x.GuildId == guildId).ToList();
    public static ShopItem Get(Realm realm, string id) => realm.Find<ShopItem>(Guid.Parse(id));
}
