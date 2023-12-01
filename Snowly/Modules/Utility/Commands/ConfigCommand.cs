using DSharpPlus;
using Snowly.Commands;
using Snowly.Modules.Utility.Commands.Config;

namespace Snowly.Modules.Utility.Commands;

public class ConfigCommand : ISlashCommandGroup
{
    public string Name => "config";
    public string Description => "Configure the bot for your server.";
    public Permissions? Permission => Permissions.ManageGuild;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[] {
        new ConfigLoggingChannelCommand()
    };
}
