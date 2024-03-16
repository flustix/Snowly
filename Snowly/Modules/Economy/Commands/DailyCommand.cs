using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Config;
using Snowly.Constants;
using Snowly.Modules.Economy.Utils;
using Snowly.Utils;

namespace Snowly.Modules.Economy.Commands;

public class DailyCommand : ISlashCommand
{
    public string Name => "daily";
    public string Description => "Claim your daily reward.";
    public bool AllowInDM => false;

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate) return;

        var user = EcoUtils.GetUser(interaction.Guild.Id, interaction.User.Id);
        var guild = Configs.GetGuildConfig(interaction.Guild.Id);

        if (!user.CanDaily)
        {
            interaction.Reply($"You can claim your daily reward again in **{FormatUtils.FormatTime(user.TimeUntilDaily, false)}**.", true);
            return;
        }

        var streakLost = user.DailyStreak > 0 && user.StreakLost;

        user.DailyStreak = user.ActualStreak + 1;
        user.Balance += 100 * user.DailyStreak;
        user.LastDaily = DateTimeOffset.UtcNow;

        EcoUtils.UpdateUser(user);

        interaction.ReplyEmbed(new CustomEmbed
            {
                Title = "Daily Reward",
                Description = $"You claimed your daily reward of **{100 * user.DailyStreak}{guild.CurrencySymbol}** {guild.CurrencyName}!",
                Footer = new CustomEmbedFooter
                {
                    Text = streakLost ? "You lost your daily streak." : $"Your daily streak is now {user.DailyStreak}."
                },
                Color = Colors.Random
            }
        );
    }
}
