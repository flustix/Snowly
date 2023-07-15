using Fluxifyed.Config;
using Realms;

namespace Fluxifyed.Database; 

public static class RealmAccess {
    private static RealmConfiguration Config => new($"{Directory.GetCurrentDirectory()}{Path.DirectorySeparatorChar}fluxifyed.realm") {
        SchemaVersion = 5,
        MigrationCallback = (migration, version) => {
            switch (version) {
                case 5:
                    foreach (var config in migration.NewRealm.All<GuildConfig>()) {
                        config.CurrencyName = "coins";
                        config.CurrencySymbol = ":coin:";
                    }
                    break;
            }
        }
    };
    
    private static Realm Realm => Realm.GetInstance(Config);

    public static void Run(Action<Realm> action) => Write(Realm, action);

    private static void Write(Realm realm, Action<Realm> func) => realm.Write(() => func(realm));
}