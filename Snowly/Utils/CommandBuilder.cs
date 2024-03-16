using DSharpPlus;
using DSharpPlus.Entities;
using Microsoft.Extensions.Logging;
using Snowly.Commands;

namespace Snowly.Utils;

public abstract class CommandBuilder
{
    public static DiscordApplicationCommand BuildCommand(ISlashCommand command)
    {
        DiscordApplicationCommand appCommand;

        switch (command)
        {
            case ISlashCommandGroup group:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    group.Subcommands.Select(s => buildSubcommand(s)),
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);

                Snowly.Logger.LogDebug($"  /{group.Name}");
                break;

            case IOptionSlashCommand optionCommand:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    optionCommand.Options.Select(buildOption),
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);

                Snowly.Logger.LogDebug(optionCommand.Options.Aggregate($"  /{command.Name}", (current, option) => current + $" {option.Name}:{option.Type.ToString()}"));
                break;

            default:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);

                Snowly.Logger.LogDebug($"  /{command.Name}");
                break;
        }

        return appCommand;
    }

    private static DiscordApplicationCommandOption buildOption(SlashOption option)
    {
        return new DiscordApplicationCommandOption(option.Name, option.Description, option.Type, option.Required, option.Choices, autocomplete: option.AutoComplete);
    }

    private static DiscordApplicationCommandOption buildSubcommand(ISlashCommand subCommand, int depth = 0)
    {
        DiscordApplicationCommandOption builder;

        var log = "  ";

        for (var i = 0; i < depth + 1; i++)
        {
            log += "  ";
        }

        log += $"| {subCommand.Name}";

        switch (subCommand)
        {
            case ISlashCommandGroup group:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommandGroup,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    group.Subcommands.Select(s => buildSubcommand(s, depth + 1)));

                Snowly.Logger.LogDebug(log);
                break;

            case IOptionSlashCommand optionCommand:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommand,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    optionCommand.Options.Select(buildOption));

                Snowly.Logger.LogDebug(optionCommand.Options.Aggregate(log, (current, option) => current + $" {option.Name}:{option.Type.ToString()}"));
                break;

            default:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommand);

                Snowly.Logger.LogDebug(log);
                break;
        }

        return builder;
    }
}
