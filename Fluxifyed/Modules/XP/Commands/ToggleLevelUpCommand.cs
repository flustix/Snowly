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
        var userConfig = Configs.GetUserConfig(interaction.User.Id);
        userConfig.LevelUpMessages = !userConfig.LevelUpMessages;
        Configs.UpdateUserConfig(userConfig);

        interaction.Reply($"Level up messages are now **{(userConfig.LevelUpMessages ? "enabled" : "disabled")}** for you globally.", true);
    }
}
