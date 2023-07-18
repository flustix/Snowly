using Realms;

namespace Fluxifyed.Modules.Welcome.Components;

public class WelcomeMessage : RealmObject {
    [PrimaryKey]
    public Guid Id { get; set; } = Guid.NewGuid();

    public string GuildId { get; set; }
    public string ChannelId { get; set; }
    public string Message { get; set; }
    public string Roles { get; set; }
}
