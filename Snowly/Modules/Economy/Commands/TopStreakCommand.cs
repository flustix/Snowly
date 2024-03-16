using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Modules.Economy.Utils;
using Snowly.Utils;

namespace Snowly.Modules.Economy.Commands;

public class TopStreakCommand : ISlashCommand
{
    public string Name => "top-streak";
    public string Description => "Shows the top 10 users with the highest daily streak";
    public bool AllowInDM => false;

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Channel.IsPrivate)
            return;

        var users = EcoUtils.GetTopStreakUsers(interaction.Guild.Id, 10);

        var description = string.Join("\n", users.Select((user, index) => $"#{index + 1} <@{user.UserID}> - {user.ActualStreak}x Streak"));
        if (users.Count == 0) description = "Nothing here...";

        interaction.ReplyEmbed(new CustomEmbed
        {
            Title = $"{interaction.Guild.Name} - Daily Streak Leaderboard",
            ThumbnailUrl = interaction.Guild.IconUrl,
            Color = Colors.Random,
            Description = description
        });
    }
}
