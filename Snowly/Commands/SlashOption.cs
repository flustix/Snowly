using DSharpPlus;
using DSharpPlus.Entities;

namespace Snowly.Commands;

public class SlashOption
{
    public string Name { get; init; }
    public string Description { get; init; }
    public ApplicationCommandOptionType Type { get; init; }
    public bool Required { get; init; }
    public bool AutoComplete { get; init; }
    public List<DiscordApplicationCommandOptionChoice> Choices { get; init; } = new();
}
