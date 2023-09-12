using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.Economy.Components;
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
        var item = ShopItem.Get(RealmAccess.Realm, id);

        if (item == null)
        {
            interaction.Reply("Invalid item.", true);
            return;
        }

        RealmAccess.Run(realm =>
        {
            interaction.ReplyEmbed(new CustomEmbed
            {
                Title = "Economy Management",
                Description = $"Removed **{item.Name}** from the shop.",
                Color = Colors.Red
            }, true);
            realm.Remove(item);
        });
    }

    public void HandleAutoComplete(DiscordInteraction interaction, DiscordInteractionDataOption focused)
    {
        var items = ShopItem.GetAllFromGuild(RealmAccess.Realm, interaction.GuildId.ToString());
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
