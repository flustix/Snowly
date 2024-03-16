using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Modules.XP.Utils;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management.LevelRoles;

public class LevelRolesRemoveCommand : IOptionSlashCommand
{
    public string Name => "remove";
    public string Description => "Remove a level role.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "role",
            Description = "The role to remove.",
            Type = ApplicationCommandOptionType.Role,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var role = interaction.GetRole("role");

        if (role is null)
        {
            interaction.Reply("The role is invalid.", true);
            return;
        }

        XpUtils.RemoveRewardRole(interaction.Guild.Id, role.Id);

        var embed = new CustomEmbed
        {
            Title = "Removed Level Role",
            Description = $"Removed the role <@&{role.Id}> from the level roles."
        };

        interaction.ReplyEmbed(embed, true);
    }
}
