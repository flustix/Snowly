using DSharpPlus.Entities;
using Midori.Logging;
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
                Logger.Log($"  /{group.Name}", level: LogLevel.Debug);

                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    group.Subcommands.Select(s => buildSubcommand(s)),
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);
                break;

            case IOptionSlashCommand optionCommand:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    optionCommand.Options.Select(buildOption),
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);

                Logger.Log(optionCommand.Options.Aggregate($"  /{command.Name}", (current, option) => current + $" {option.Name}:{option.Type.ToString()}"), level: LogLevel.Debug);
                break;

            default:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    allowDMUsage: command.AllowInDM,
                    defaultMemberPermissions: command.Permission);

                Logger.Log($"  /{command.Name}", level: LogLevel.Debug);
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
                Logger.Log(log, level: LogLevel.Debug);

                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    DiscordApplicationCommandOptionType.SubCommandGroup,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    group.Subcommands.Select(s => buildSubcommand(s, depth + 1)));
                break;

            case IOptionSlashCommand optionCommand:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    DiscordApplicationCommandOptionType.SubCommand,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    optionCommand.Options.Select(buildOption));

                Logger.Log(optionCommand.Options.Aggregate(log, (current, option) => current + $" {option.Name}{(option.Required ? "" : "?")}:{option.Type.ToString()}"), level: LogLevel.Debug);
                break;

            default:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    DiscordApplicationCommandOptionType.SubCommand);

                Logger.Log(log, level: LogLevel.Debug);
                break;
        }

        return builder;
    }
}
