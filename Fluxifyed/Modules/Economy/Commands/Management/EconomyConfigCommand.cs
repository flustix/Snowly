using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Config;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Economy.Commands.Management;

public class EconomyConfigCommand : IOptionSlashCommand {
    public string Name => "config";
    public string Description => "Configure the economy of your server.";
    public Permissions? Permission => Permissions.ManageGuild;

    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "setting",
            Description = "The setting to configure.",
            Type = ApplicationCommandOptionType.String,
            Required = true,
            Choices = new List<DiscordApplicationCommandOptionChoice> {
                new("Currency Name", "currency-name"),
                new("Currency Symbol", "currency-symbol")
            }
        },
        new SlashOption {
            Name = "value",
            Description = "The value to set the setting to.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction) {
        var setting = interaction.GetString("setting");
        var value = interaction.GetString("value");

        RealmAccess.Run(realm => {
            switch (setting) {
                case "currency-name":
                    var guild = GuildConfig.GetOrCreate(realm, interaction.Guild.Id.ToString());
                    guild.CurrencyName = value;
                    interaction.ReplyEmbed(new CustomEmbed {
                        Title = "Economy Configuration",
                        Description = $"Set the currency name to **{value}**.",
                        Color = Colors.Random
                    }, true);
                    break;

                case "currency-symbol":
                    guild = GuildConfig.GetOrCreate(realm, interaction.Guild.Id.ToString());
                    guild.CurrencySymbol = value;
                    interaction.ReplyEmbed(new CustomEmbed {
                        Title = "Economy Configuration",
                        Description = $"Set the currency symbol to **{value}**.",
                        Color = Colors.Random
                    }, true);
                    break;

                default:
                    interaction.ReplyEmbed(new CustomEmbed {
                        Title = "Economy Configuration",
                        Description = $"Unknown setting **{setting}**.",
                        Color = Colors.Random
                    }, true);
                    break;
            }
        });
    }
}
