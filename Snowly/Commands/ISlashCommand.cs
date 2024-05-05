using DSharpPlus.Entities;
using Snowly.Utils;

namespace Snowly.Commands;

/// <summary>
/// A basic slash command.
/// </summary>
public interface ISlashCommand
{
    /// <summary>
    /// The name of the command.
    /// Needs to be lowercase and can't contain spaces.
    /// Limited to 32 characters.
    /// </summary>
    string Name { get; }

    /// <summary>
    /// The description of the command.
    /// Limited to 100 characters.
    /// </summary>
    string Description { get; }

    /// <summary>
    /// Default permissions to run this command.
    /// </summary>
    DiscordPermissions? Permission => null;

    /// <summary>
    /// Whether this command can be used in direct messages.
    /// </summary>
    bool AllowInDM { get; }

    void Handle(DiscordInteraction interaction);
    void HandleAutoComplete(DiscordInteraction interaction, DiscordInteractionDataOption focused) => interaction.ReplyAutoComplete(new List<DiscordAutoCompleteChoice>());
}
