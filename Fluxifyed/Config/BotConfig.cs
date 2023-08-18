namespace Fluxifyed.Config;

public class BotConfig {
    public string Token { get; set; }
    public string DefaultFont { get; set; } = "Arial";
    public Dictionary<string, string> Fonts { get; set; } = new();
}
