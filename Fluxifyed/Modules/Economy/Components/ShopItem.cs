using Realms;

namespace Fluxifyed.Modules.Economy.Components;

public class ShopItem
{
    public Guid Id { get; init; } = Guid.NewGuid();

    public ulong GuildId { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Icon { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public int Price { get; set; }
}
