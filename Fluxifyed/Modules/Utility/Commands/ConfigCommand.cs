using DSharpPlus;
using Fluxifyed.Commands;
using Fluxifyed.Modules.Utility.Commands.Config;

namespace Fluxifyed.Modules.Utility.Commands;

public class ConfigCommand : ISlashCommandGroup
{
    public string Name => "config";
    public string Description => "Configure the bot for your server.";
    public Permissions? Permission => Permissions.ManageGuild;

    public IEnumerable<ISlashCommand> Subcommands => new ISlashCommand[] {
        new ConfigLoggingChannelCommand()
    };
}
