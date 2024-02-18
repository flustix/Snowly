using JetBrains.Annotations;

namespace Snowly.Config;

[UsedImplicitly(ImplicitUseTargetFlags.WithMembers)]
public class BotConfig
{
    public string MongoConnection { get; init; } = "mongodb://localhost:27017";
    public string Database { get; init; } = "snowly";
    public string Token { get; init; }
    public string DefaultFont { get; init; } = "Arial";
    public Dictionary<string, string> Fonts { get; init; } = new();
}
