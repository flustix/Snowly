namespace Snowly.Commands;

/// <summary>
/// A slash command with options.
/// </summary>
public interface IOptionSlashCommand : ISlashCommand
{
    List<SlashOption> Options { get; }
}
