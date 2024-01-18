namespace Snowly.Commands;

/// <summary>
/// A slash command with options.
/// </summary>
public interface IOptionSlashCommand : ISlashCommand
{
    /// <summary>
    /// The options of this command.
    /// </summary>
    List<SlashOption> Options { get; }
}
