using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Modules.XP.Utils;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management.LevelRoles;

public class LevelRolesAddCommand : IOptionSlashCommand
{
    public string Name => "add";
    public string Description => "Add a level role.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "level",
            Description = "The level required to get this role.",
            Type = DiscordApplicationCommandOptionType.Integer,
            Required = true
        },
        new SlashOption
        {
            Name = "role",
            Description = "The role to give.",
            Type = DiscordApplicationCommandOptionType.Role,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var level = interaction.GetInt("level");
        var role = interaction.GetRole("role");

        if (level is null)
        {
            interaction.Reply("The level is invalid.", true);
            return;
        }

        if (level < 1)
        {
            interaction.Reply("The level must be greater than 0.", true);
            return;
        }

        if (role is null)
        {
            interaction.Reply("The role is invalid.", true);
            return;
        }

        var roles = XpUtils.GetRewardRoles(interaction.Guild.Id);

        if (roles.Any(x => x.RoleId == role.Id))
        {
            interaction.Reply("The role is already a level role.", true);
            return;
        }

        XpUtils.AddRewardRole(interaction.Guild.Id, level.Value, role.Id);

        var embed = new CustomEmbed
        {
            Title = "Added Level Role",
            Description = $"Added <@&{role.Id}> to level **{level}**."
        };

        interaction.ReplyEmbed(embed, true);
    }
}
