using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Microsoft.Extensions.Logging;

namespace Fluxifyed.Utils;

public abstract class CommandBuilder {
    public static DiscordApplicationCommand BuildCommand(ISlashCommand command) {
        DiscordApplicationCommand appCommand;

        switch (command) {
            case ISlashCommandGroup group:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    group.Subcommands.Select(s => buildSubcommand(s)),
                    defaultMemberPermissions: command.Permission);

                Fluxifyed.Logger.LogDebug($"  /{group.Name}");
                break;

            case IOptionSlashCommand optionCommand:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    optionCommand.Options.Select(buildOption),
                    defaultMemberPermissions: command.Permission);

                Fluxifyed.Logger.LogDebug(optionCommand.Options.Aggregate($"  /{command.Name}", (current, option) => current + $" {option.Name}:{option.Type.ToString()}"));
                break;

            default:
                appCommand = new DiscordApplicationCommand(
                    command.Name,
                    command.Description,
                    defaultMemberPermissions: command.Permission);

                Fluxifyed.Logger.LogDebug($"  /{command.Name}");
                break;
        }

        return appCommand;
    }

    private static DiscordApplicationCommandOption buildOption(SlashOption option) {
        return new DiscordApplicationCommandOption(option.Name, option.Description, option.Type, option.Required, option.Choices);
    }

    private static DiscordApplicationCommandOption buildSubcommand(ISlashCommand subCommand, int depth = 0) {
        DiscordApplicationCommandOption builder;

        var log = "  ";

        for (var i = 0; i < depth + 1; i++) {
            log += "  ";
        }

        log += $"| {subCommand.Name}";

        switch (subCommand) {
            case ISlashCommandGroup group:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommandGroup,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    group.Subcommands.Select(s => buildSubcommand(s, depth + 1)));

                Fluxifyed.Logger.LogDebug(log);
                break;

            case IOptionSlashCommand optionCommand:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommand,
                    null,
                    Array.Empty<DiscordApplicationCommandOptionChoice>(),
                    optionCommand.Options.Select(buildOption));

                Fluxifyed.Logger.LogDebug(optionCommand.Options.Aggregate(log, (current, option) => current + $" {option.Name}:{option.Type.ToString()}"));
                break;

            default:
                builder = new DiscordApplicationCommandOption(
                    subCommand.Name,
                    subCommand.Description,
                    ApplicationCommandOptionType.SubCommand);

                Fluxifyed.Logger.LogDebug(log);
                break;
        }

        return builder;
    }
}
