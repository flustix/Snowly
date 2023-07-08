using DSharpPlus;
using DSharpPlus.Entities;

namespace Fluxifyed.Commands; 

/// <summary>
/// A slash command that contains subcommands.
/// </summary>
public interface ISlashCommandGroup : ISlashCommand {
    ISlashCommand[] Subcommands { get; }
    string ISlashCommand.Description => "missing description";

    void ISlashCommand.Handle(DiscordInteraction interaction) {
        var subcommand = interaction.Data.Options.First().Name;
        var command = Subcommands.FirstOrDefault(x => x.Name == subcommand);
        
        if (command is null) {
            interaction.CreateResponseAsync(InteractionResponseType.ChannelMessageWithSource, new DiscordInteractionResponseBuilder {
                Content = "Subcommand not found."
            });
            return;
        }
        
        command.Handle(interaction);
    }
}