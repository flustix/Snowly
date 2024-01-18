using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands;

public class ToggleLevelUpCommand : ISlashCommand
{
    public string Name => "toggle-level-up";
    public string Description => "Toggle level up messages for (only) you globally.";

    public void Handle(DiscordInteraction interaction)
    {
        var userConfig = Configs.GetUserConfig(interaction.User.Id);
        userConfig.LevelUpMessages = !userConfig.LevelUpMessages;
        Configs.UpdateUserConfig(userConfig);

        interaction.Reply($"Level up messages are now **{(userConfig.LevelUpMessages ? "enabled" : "disabled")}** for you globally.", true);
    }
}
