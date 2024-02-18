using MongoDB.Driver;

namespace Snowly.Database;

public static class MongoDatabase
{
    private static IMongoDatabase database = null!;

    public static void Setup(string conn, string db)
    {
        var client = new MongoClient(conn);
        database = client.GetDatabase(db);
    }

    public static IMongoCollection<T> GetCollection<T>(string name) => database.GetCollection<T>(name);
}
