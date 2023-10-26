using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Database;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands.Management;

public class EnableXpCommand : ISlashCommand {
    public string Name => "enabled";
    public string Description => "Enable or disable XP collection for this server.";

    public void Handle(DiscordInteraction interaction) {
        if (interaction.Channel.IsPrivate) return;

        var guildConfig = Configs.GetGuildConfig(interaction.Guild.Id);
        guildConfig.XpEnabled = !guildConfig.XpEnabled;
        Configs.UpdateGuildConfig(guildConfig);

        interaction.Reply($"XP collection is now **{(guildConfig.XpEnabled ? "enabled" : "disabled")}** for this server.", true);
    }
}
