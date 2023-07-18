using DSharpPlus;
using DSharpPlus.Entities;

namespace Fluxifyed.Commands;

/// <summary>
/// A basic slash command.
/// </summary>
public interface ISlashCommand {
    string Name { get; }
    string Description { get; }
    Permissions? Permission => null;

    void Handle(DiscordInteraction interaction);
}
