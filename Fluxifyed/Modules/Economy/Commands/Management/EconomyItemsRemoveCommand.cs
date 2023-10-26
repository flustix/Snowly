using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.Economy.Components;
using Fluxifyed.Modules.Economy.Utils;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Economy.Commands.Management;

public class EconomyItemsRemoveCommand : IOptionSlashCommand
{
    public string Name => "remove";
    public string Description => "Remove an item from the shop.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "item",
            Description = "The item to remove.",
            Type = ApplicationCommandOptionType.String,
            Required = true,
            AutoComplete = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var id = interaction.GetString("item");
        var item = ShopItemUtils.Get(id);

        if (item == null)
        {
            interaction.Reply("Invalid item.", true);
            return;
        }

        ShopItemUtils.Remove(item);

        interaction.ReplyEmbed(new CustomEmbed
        {
            Title = "Economy Management",
            Description = $"Removed **{item.Name}** from the shop.",
            Color = Colors.Red
        }, true);
    }

    public void HandleAutoComplete(DiscordInteraction interaction, DiscordInteractionDataOption focused)
    {
        var items = ShopItemUtils.GetAllFromGuild(interaction.GuildId ?? 0);
        var choices = new List<DiscordAutoCompleteChoice>();
        var value = interaction.GetString(focused.Name);

        foreach (var item in items)
        {
            if (string.IsNullOrEmpty(value) || string.IsNullOrWhiteSpace(value) || item.Name.ToLower().Contains(value.ToLower()))
                choices.Add(new DiscordAutoCompleteChoice(item.Name, item.Id.ToString()));
        }

        interaction.ReplyAutoComplete(choices);
    }
}
