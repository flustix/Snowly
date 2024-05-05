using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Modules.Utility.Commands.Config;

namespace Snowly.Modules.Utility.Commands;

public class ConfigCommand : ISlashCommandGroup
{
    public string Name => "config";
    public string Description => "Configure the bot for your server.";
    public DiscordPermissions? Permission => DiscordPermissions.ManageGuild;
    public bool AllowInDM => false;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[]
    {
        new ConfigLoggingChannelCommand()
    };
}
