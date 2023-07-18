using Realms;

namespace Fluxifyed.Modules.Timers.Components;

public class Timer : RealmObject {
    [PrimaryKey]
    public Guid Id { get; set; } = Guid.NewGuid();

    public string GuildId { get; set; }
    public string ChannelId { get; set; }

    public int Hour { get; set; }
    public int Minute { get; set; }

    public string Message { get; set; }
    public string Random { get; set; }

    public int AntiRepeat { get; set; }
    public string AntiRepeatHistory { get; set; } = string.Empty;
}
