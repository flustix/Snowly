using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Utils;

namespace Fluxifyed.Commands;

/// <summary>
/// A basic slash command.
/// </summary>
public interface ISlashCommand {
    string Name { get; }
    string Description { get; }
    Permissions? Permission => null;

    void Handle(DiscordInteraction interaction);
    void HandleAutoComplete(DiscordInteraction interaction, DiscordInteractionDataOption focused) => interaction.ReplyAutoComplete(new List<DiscordAutoCompleteChoice>());
}
