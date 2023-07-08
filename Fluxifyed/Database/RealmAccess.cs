using Realms;

namespace Fluxifyed.Database; 

public static class RealmAccess {
    private static RealmConfiguration Config => new($"{Directory.GetCurrentDirectory()}{Path.DirectorySeparatorChar}fluxifyed.realm") {
        SchemaVersion = 4
    };
    
    private static Realm Realm => Realm.GetInstance(Config);

    public static void Run(Action<Realm> action) => Write(Realm, action);

    private static void Write(Realm realm, Action<Realm> func) => realm.Write(() => func(realm));
}