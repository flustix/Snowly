using Fluxifyed.Config;
using Realms;

namespace Fluxifyed.Database;

public static class RealmAccess {
    private static RealmConfiguration config => new($"{Directory.GetCurrentDirectory()}{Path.DirectorySeparatorChar}fluxifyed.realm") {
        SchemaVersion = 5,
        MigrationCallback = (migration, version) => {
            switch (version) {
                case 5:
                    foreach (var guildconf in migration.NewRealm.All<GuildConfig>()) {
                        guildconf.CurrencyName = "coins";
                        guildconf.CurrencySymbol = ":coin:";
                    }

                    break;
            }
        }
    };

    public static Realm Realm => Realm.GetInstance(config);

    public static void Run(Action<Realm> action) => write(Realm, action);

    private static void write(Realm realm, Action<Realm> func) => realm.Write(() => func(realm));
}
