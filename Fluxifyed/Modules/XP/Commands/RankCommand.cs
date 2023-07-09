using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.XP.Utils;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace Fluxifyed.Modules.XP.Commands; 

public class RankCommand : IOptionSlashCommand {
    public string Name => "rank";
    public string Description => "Shows your rank";
    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "user",
            Description = "The user to get the rank of",
            Type = ApplicationCommandOptionType.User,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction) {
        await interaction.Acknowledge();
        
        var member = await interaction.GetUser("user") ?? interaction.User;
        
        RealmAccess.Run(realm => {
            if (interaction.Channel.IsPrivate) return;
            var user = XpUtils.GetUser(realm, interaction.Guild.Id.ToString(), member.Id.ToString());

            interaction.FollowupEmbed(new CustomEmbed { 
                    Author = new CustomEmbedAuthor {
                        Name = member.GetUsername(),
                        IconUrl = member.GetAvatarUrl(ImageFormat.Auto)
                    },
                    Color = Colors.Random,
                    Fields = new List<CustomEmbedField> {
                        new() {
                            Name = ":trophy: Rank",
                            Value = $"#{XpUtils.GetRank(realm, interaction.Guild.Id.ToString(),member.Id.ToString())}",
                            Inline = true
                        },
                        new() {
                            Name = ":star: XP",
                            Value = $"{user.Xp}",
                            Inline = true
                        },
                        new() {
                            Name = ":1234: Level",
                            Value = $"{user.Level}",
                            Inline = true
                        },
                        new() {
                            Name = ":1234: XP Left",
                            Value = $"{user.XpLeft}",
                            Inline = true
                        },
                        new() {
                            Name = ":symbols: Progress",
                            Value = $"{user.LevelProgressPercent:P2} ({user.LevelProgress}/{user.XpFromCurrentToNext})".Replace(",", "."),
                            Inline = true
                        }
                    }
                }
            );
        });
    }
}