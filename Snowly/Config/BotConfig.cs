using JetBrains.Annotations;

namespace Snowly.Config;

[UsedImplicitly(ImplicitUseTargetFlags.WithMembers)]
public class BotConfig
{
    public string Database { get; init; } = "snowly";
    public string Token { get; init; }
    public string DefaultFont { get; init; } = "Arial";
    public Dictionary<string, string> Fonts { get; init; } = new();
}
