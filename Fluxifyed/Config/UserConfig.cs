using Realms;

namespace Fluxifyed.Config;

public class UserConfig : RealmObject {
    [PrimaryKey]
    public string UserId { get; set; } = "";

    // XP
    public bool LevelUpMessages { get; set; } = true;

    public static UserConfig GetOrCreate(Realm realm, string userId) {
        var config = realm.Find<UserConfig>(userId);
        if (config is null) realm.Add(config = new UserConfig { UserId = userId });

        return config;
    }
}
