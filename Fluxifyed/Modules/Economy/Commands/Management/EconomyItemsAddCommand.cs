using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Database;
using Fluxifyed.Modules.Economy.Components;
using Fluxifyed.Modules.Economy.Utils;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Economy.Commands.Management;

public class EconomyItemsAddCommand : IOptionSlashCommand
{
    public string Name => "add";
    public string Description => "Add an item to the shop.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "name",
            Description = "The name of the item.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption
        {
            Name = "icon",
            Description = "The icon of the item.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption
        {
            Name = "price",
            Description = "The price of the item.",
            Type = ApplicationCommandOptionType.Integer,
            Required = true
        },
        new SlashOption
        {
            Name = "description",
            Description = "The description of the item.",
            Type = ApplicationCommandOptionType.String,
            Required = false
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var name = interaction.GetString("name");
        var icon = interaction.GetString("icon");
        var price = interaction.GetInt("price");
        var description = interaction.GetString("description") ?? "No description.";

        if (name == null || icon == null || price == null)
        {
            interaction.Reply("Invalid arguments.", true);
            return;
        }

        RealmAccess.Run(realm =>
        {
            var item = new ShopItem
            {
                Name = name,
                Icon = icon,
                Price = price.Value,
                Description = description,
                GuildId = interaction.Guild.Id.ToString()
            };

            realm.Add(item);

            var guild = GuildConfig.GetOrCreate(realm, interaction.Guild.Id.ToString());
            interaction.Reply($"Added {item.Icon}**{item.Name}** for **{item.Price}**{guild.CurrencySymbol} {guild.CurrencyName}.", true);
        });
    }
}
