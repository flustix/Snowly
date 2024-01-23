using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Modules.XP.Utils;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management.LevelRoles;

public class LevelRolesListCommand : ISlashCommand
{
    public string Name => "list";
    public string Description => "List all level roles.";

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate) return;

        var roles = XpUtils.GetRewardRoles(interaction.Guild.Id);
        roles.Sort((x, y) => x.Level.CompareTo(y.Level));

        var embed = new CustomEmbed
        {
            Title = "Level Roles",
            Description = string.Join("\n", roles.Select(x => $"**{x.Level}** - <@&{x.RoleId}>"))
        };

        interaction.ReplyEmbed(embed, true);
    }
}
