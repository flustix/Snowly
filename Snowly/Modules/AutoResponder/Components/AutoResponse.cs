using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Snowly.Modules.AutoResponder.Components;

public class AutoResponse
{
    [BsonId]
    public ObjectId ID { get; init; } = ObjectId.GenerateNewId();

    [BsonElement("gid")]
    public ulong GuildID { get; init; }

    [BsonElement("cid")]
    public ulong ChannelID { get; init; } = 0;

    [BsonElement("trigger")]
    public string Trigger { get; init; }

    [BsonElement("response")]
    public string Response { get; init; }
}
