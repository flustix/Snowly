using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.XP.Utils;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands;

public class CalculateCommand : IOptionSlashCommand {
    public string Name => "calculate-level";
    public string Description => "Calculate the amount of XP and time you need to reach a certain level.";

    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "level",
            Description = "The level you want to reach",
            Type = ApplicationCommandOptionType.Integer,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction) {
        RealmAccess.Run(realm => {
            if (interaction.Channel.IsPrivate) return;
            var user = XpUtils.GetUser(realm, interaction.Guild.Id.ToString(), interaction.User.Id.ToString());

            long level = interaction.GetInt("level") ?? 0;

            if (level < 1) {
                interaction.Reply("You can't reach a level lower than 1.", true);
                return;
            }

            if (level <= user.Level) {
                interaction.Reply("You can't reach a level lower than your current level.", true);
                return;
            }

            const int rng_min = 10;
            const int rng_max = 20;
            const int cooldown = 60;

            var xpNeeded = XpUtils.GetXpForLevel(level);
            var xpLeft = xpNeeded - user.Xp;

            var messagesMin = (long)Math.Ceiling((float)xpLeft / rng_max);
            var messagesMax = (long)Math.Ceiling((float)xpLeft / rng_min);
            var messagesAvg = (long)Math.Ceiling(xpLeft / ((rng_min + rng_max) / 2f));
            var time = messagesAvg * cooldown * 1000;

            interaction.ReplyEmbed(new CustomEmbed {
                    Title = $"Level {level}",
                    Color = Colors.Random,
                    Fields = new List<CustomEmbedField> {
                        new() {
                            Name = "Current XP",
                            Value = $"{user.Xp}",
                            Inline = true
                        },
                        new() {
                            Name = "Needed XP",
                            Value = $"{xpNeeded}",
                            Inline = true
                        },
                        new() {
                            Name = "XP Left",
                            Value = $"{xpLeft}",
                            Inline = true
                        },
                        new() {
                            Name = "XP per message",
                            Value = $"{rng_min} - {rng_max}",
                            Inline = true
                        },
                        new() {
                            Name = "Messages needed",
                            Value = $"{messagesMin}-{messagesMax} (avg. {messagesAvg})",
                            Inline = true
                        },
                        new() {
                            Name = "Time needed",
                            Value = $"{FormatUtils.FormatTime(time)}",
                            Inline = true
                        }
                    }
                }
            );
        });
    }
}
