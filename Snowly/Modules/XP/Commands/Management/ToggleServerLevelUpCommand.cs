using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management;

public class ToggleServerLevelUpCommand : ISlashCommand
{
    public string Name => "level-up";
    public string Description => "Toggle level up messages for the entire server.";
    public bool AllowInDM => false;

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate) return;

        var guildConfig = Configs.GetGuildConfig(interaction.Guild.Id);
        guildConfig.LevelUpMessages = !guildConfig.LevelUpMessages;
        Configs.UpdateGuildConfig(guildConfig);

        interaction.Reply($"Level up messages are now **{(guildConfig.LevelUpMessages ? "enabled" : "disabled")}** for the entire server.", true);
    }
}
