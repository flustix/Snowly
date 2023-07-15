using DSharpPlus;
using DSharpPlus.Entities;

namespace Fluxifyed.Commands; 

public class SlashOption {
    public string Name { get; set; }
    public string Description { get; set; }
    public ApplicationCommandOptionType Type { get; set; }
    public bool Required { get; set; }
    public List<DiscordApplicationCommandOptionChoice> Choices { get; set; } = new();
}