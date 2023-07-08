using Fluxifyed.Database;
using Realms;

namespace Fluxifyed.Config; 

public class GuildConfig : RealmObject {
    [PrimaryKey]
    public string GuildId { get; set; } = "";
    
    // XP
    public bool XpEnabled { get; set; } = true;
    public bool LevelUpMessages { get; set; } = true;
    public string LevelUpChannelId { get; set; } = "";

    public static GuildConfig GetOrCreate(Realm realm, string guildId) {
        var config = realm.Find<GuildConfig>(guildId);
        
        if (config is null) realm.Add(config = new GuildConfig { GuildId = guildId });
        
        return config;
    }
}