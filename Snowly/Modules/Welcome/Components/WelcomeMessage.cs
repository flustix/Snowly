namespace Snowly.Modules.Welcome.Components;

public class WelcomeMessage
{
    public Guid Id { get; set; } = Guid.NewGuid();

    public ulong GuildId { get; set; } = 0;
    public ulong ChannelId { get; set; } = 0;
    public string Message { get; set; } = string.Empty;
    public List<ulong> Roles { get; set; } = new();
}
