using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Database;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands;

public class ToggleLevelUpCommand : ISlashCommand {
    public string Name => "toggle-level-up";
    public string Description => "Toggle level up messages for (only) you globally.";

    public void Handle(DiscordInteraction interaction) {
        RealmAccess.Run(realm => {
            var userConfig = UserConfig.GetOrCreate(realm, interaction.User.Id.ToString());
            userConfig.LevelUpMessages = !userConfig.LevelUpMessages;

            interaction.Reply($"Level up messages are now **{(userConfig.LevelUpMessages ? "enabled" : "disabled")}** for you globally.", true);
        });
    }
}
