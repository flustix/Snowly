using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management;

public class EnableXpCommand : ISlashCommand
{
    public string Name => "enabled";
    public string Description => "Enable or disable XP collection for this server.";
    public bool AllowInDM => false;

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate) return;

        var guildConfig = Configs.GetGuildConfig(interaction.Guild.Id);
        guildConfig.XPEnabled = !guildConfig.XPEnabled;
        Configs.UpdateGuildConfig(guildConfig);

        interaction.Reply($"XP collection is now **{(guildConfig.XPEnabled ? "enabled" : "disabled")}** for this server.", true);
    }
}
