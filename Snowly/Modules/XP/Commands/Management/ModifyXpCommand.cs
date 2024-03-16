using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Modules.XP.Utils;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management;

public class ModifyXpCommand : IOptionSlashCommand
{
    public string Name => "modify";
    public string Description => "Modify the XP of a user.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "user",
            Description = "The user to modify the XP of.",
            Type = ApplicationCommandOptionType.User,
            Required = true
        },
        new SlashOption
        {
            Name = "amount",
            Description = "The amount of XP to modify.",
            Type = ApplicationCommandOptionType.Integer,
            Required = true
        },
        new SlashOption
        {
            Name = "action",
            Description = "What to do with the XP. (add, remove, set)",
            Type = ApplicationCommandOptionType.String
        }
    };

    public async void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate) return;

        var user = await interaction.GetUser("user");
        var amount = interaction.GetInt("amount") ?? 0;
        var action = interaction.GetString("action") ?? "add";

        if (user == null) throw new Exception("Member not found.");
        if (amount == 0) throw new Exception("Amount not found.");

        var target = XpUtils.GetUser(interaction.Guild.Id, user.Id);

        target.Xp += action switch
        {
            "add" => amount,
            "remove" => -amount,
            "set" => amount - target.Xp,
            _ => throw new Exception("Invalid action.")
        };

        var actionString = action switch
        {
            "add" => "Added",
            "remove" => "Removed",
            "set" => "Set",
            _ => throw new Exception("Invalid action.")
        };

        XpUtils.UpdateUser(target);

        interaction.ReplyEmbed(new CustomEmbed
        {
            Title = "XP Modified",
            Description = $"{actionString} {amount} XP to {user.Mention}",
            Color = Colors.Success
        }, true);
    }
}
