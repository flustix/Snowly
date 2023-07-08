using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.XP.Utils;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands; 

public class TopCommand : ISlashCommand {
    public string Name => "top-xp";
    public string Description => "Shows the top 10 users with the most XP.";
    
    public void Handle(DiscordInteraction interaction) {
        RealmAccess.Run(realm => {
            if (interaction.Channel.IsPrivate) return;
            var users = XpUtils.GetTopUsers(realm, interaction.Guild.Id.ToString(), 10);
            
            interaction.ReplyEmbed(new CustomEmbed {
                Title = $"{interaction.Guild.Name} - XP Leaderboard",
                ThumbnailUrl = interaction.Guild.IconUrl,
                Color = Colors.Random,
                Description = string.Join("\n", users.Select((user, index) => $"#{index + 1} <@{user.UserId}> - {user.Xp} XP | {user.Level}"))
            });
        });
    }
}