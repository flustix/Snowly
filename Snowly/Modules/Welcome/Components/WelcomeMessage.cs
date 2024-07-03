namespace Snowly.Modules.Welcome.Components;

public class WelcomeMessage
{
    public Guid Id { get; set; } = Guid.NewGuid();

    public ulong GuildId { get; set; }
    public ulong ChannelId { get; set; }
    public string Message { get; set; } = string.Empty;
    public List<ulong> Roles { get; set; } = new();
}
