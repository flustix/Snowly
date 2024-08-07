﻿using Snowly.Commands;
using Snowly.Modules.Utility.Commands;

namespace Snowly.Modules.Utility;

public class UtilityModule : IModule
{
    public string Name => "Utility";
    public string Description => "Utility commands for your server.";

    public List<ISlashCommand> SlashCommands => new()
    {
        new AboutCommand(),
        new AccentColorCommand(),
        new AvatarCommand(),
        new BeanCommand(),
        new ConfigCommand(),
        new GuildInfoCommand(),
        new LuaCommand(),
        new PingCommand(),
        new SayCommand(),
        new SayJsonCommand()
    };
}
