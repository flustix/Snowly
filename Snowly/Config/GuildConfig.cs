namespace Snowly.Config;

public class GuildConfig {
    public ulong Id { get; set; }

    // General
    public ulong LoggingChannelId { get; set; }

    // XP
    public bool XpEnabled { get; set; } = true;
    public bool LevelUpMessages { get; set; } = true;
    public ulong LevelUpChannelId { get; set; }

    // Economy
    public string CurrencyName { get; set; } = "coins";
    public string CurrencySymbol { get; set; } = ":coin:";
}
