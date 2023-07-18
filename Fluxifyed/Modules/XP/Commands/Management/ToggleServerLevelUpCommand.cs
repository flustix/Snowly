using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Database;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands.Management;

public class ToggleServerLevelUpCommand : ISlashCommand {
    public string Name => "level-up";
    public string Description => "Toggle level up messages for the entire server.";

    public void Handle(DiscordInteraction interaction) {
        if (interaction.Channel.IsPrivate) return;

        RealmAccess.Run(realm => {
            var guildConfig = GuildConfig.GetOrCreate(realm, interaction.Guild.Id.ToString());
            guildConfig.LevelUpMessages = !guildConfig.LevelUpMessages;

            interaction.Reply($"Level up messages are now **{(guildConfig.LevelUpMessages ? "enabled" : "disabled")}** for the entire server.", true);
        });
    }
}
