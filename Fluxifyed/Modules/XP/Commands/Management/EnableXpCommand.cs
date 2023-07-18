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
        RealmAccess.Run(realm => {
            if (interaction.Channel.IsPrivate) return;
            var guildConfig = GuildConfig.GetOrCreate(realm, interaction.Guild.Id.ToString());
            guildConfig.XpEnabled = !guildConfig.XpEnabled;

            interaction.Reply($"XP collection is now **{(guildConfig.XpEnabled ? "enabled" : "disabled")}** for this server.", true);
        });
    }
}
