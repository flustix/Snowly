using DSharpPlus.Entities;
using Snowly.Utils;

namespace Snowly.Commands;

/// <summary>
/// A slash command that contains subcommands.
/// </summary>
public interface ISlashCommandGroup : ISlashCommand
{
    /// <summary>
    /// The depth of the subcommand.
    /// </summary>
    int Depth => 1;

    /// <summary>
    /// The subcommands of this command.
    /// </summary>
    IEnumerable<ISlashCommand> Subcommands { get; }

    void ISlashCommand.Handle(DiscordInteraction interaction)
    {
        var option = interaction.Data.Options.First();
        var subcommand = option.Name;

        for (var i = 0; i < Depth - 1; i++)
        {
            option = option.Options.First();
            subcommand = option.Name;
        }

        var command = Subcommands.FirstOrDefault(x => x.Name == subcommand);

        if (command is null)
        {
            interaction.Reply("Subcommand not found.", true);
            return;
        }

        command.Handle(interaction);
    }

    void ISlashCommand.HandleAutoComplete(DiscordInteraction interaction, DiscordInteractionDataOption focused)
    {
        var option = interaction.Data.Options.First();
        var subcommand = option.Name;

        for (var i = 0; i < Depth - 1; i++)
        {
            option = option.Options.First();
            subcommand = option.Name;
        }

        var command = Subcommands.FirstOrDefault(x => x.Name == subcommand);

        if (command is null)
        {
            interaction.Reply("Subcommand not found.", true);
            return;
        }

        command.HandleAutoComplete(interaction, focused);
    }
}
