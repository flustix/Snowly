using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Config;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Economy.Commands.Management;

public class EconomyConfigCommand : IOptionSlashCommand
{
    public string Name => "config";
    public string Description => "Configure the economy of your server.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "setting",
            Description = "The setting to configure.",
            Type = ApplicationCommandOptionType.String,
            Required = true,
            Choices = new List<DiscordApplicationCommandOptionChoice>
            {
                new("Currency Name", "currency-name"),
                new("Currency Symbol", "currency-symbol")
            }
        },
        new SlashOption
        {
            Name = "value",
            Description = "The value to set the setting to.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var setting = interaction.GetString("setting");
        var value = interaction.GetString("value");

        var config = Configs.GetGuildConfig(interaction.Guild.Id);

        switch (setting)
        {
            case "currency-name":
                config.CurrencyName = value;
                Configs.UpdateGuildConfig(config);
                interaction.ReplyEmbed(new CustomEmbed
                {
                    Title = "Economy Configuration",
                    Description = $"Set the currency name to **{value}**.",
                    Color = Colors.Random
                }, true);
                break;

            case "currency-symbol":
                config.CurrencySymbol = value;
                Configs.UpdateGuildConfig(config);
                interaction.ReplyEmbed(new CustomEmbed
                {
                    Title = "Economy Configuration",
                    Description = $"Set the currency symbol to **{value}**.",
                    Color = Colors.Random
                }, true);
                break;

            default:
                interaction.ReplyEmbed(new CustomEmbed
                {
                    Title = "Economy Configuration",
                    Description = $"Unknown setting **{setting}**.",
                    Color = Colors.Random
                }, true);
                break;
        }
    }
}
