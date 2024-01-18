using DSharpPlus;
using DSharpPlus.Entities;

namespace Snowly.Commands;

public class SlashOption
{
    /// <summary>
    /// The name of this option.
    /// Needs to be lowercase and can't contain spaces.
    /// Limited to 32 characters.
    /// </summary>
    public string Name { get; init; }

    /// <summary>
    /// The description of this option.
    /// Limited to 100 characters.
    /// </summary>
    public string Description { get; init; }

    /// <summary>
    /// The type of this option.
    /// </summary>
    public ApplicationCommandOptionType Type { get; init; }

    /// <summary>
    /// Whether this option is required.
    /// </summary>
    public bool Required { get; init; }

    /// <summary>
    /// Whether this option has autocompletion.
    /// </summary>
    public bool AutoComplete { get; init; }

    /// <summary>
    /// The choices of this option.
    /// </summary>
    public List<DiscordApplicationCommandOptionChoice> Choices { get; init; }
}
