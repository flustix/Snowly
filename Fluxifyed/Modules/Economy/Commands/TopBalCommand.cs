using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Config;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.Economy.Utils;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Economy.Commands;

public class TopBalCommand : ISlashCommand {
    public string Name => "top-bal";
    public string Description => "Shows the top 10 richest users";

    public void Handle(DiscordInteraction interaction) {
        if (interaction.Channel.IsPrivate) return;
        var users = EcoUtils.GetTopBalUsers(interaction.Guild.Id, 10);
        var guild = Configs.GetGuildConfig(interaction.Guild.Id);

        var description = string.Join("\n", users.Select((user, index) => $"#{index + 1} <@{user.UserId}> - {user.Balance}{guild.CurrencySymbol}"));
        if (users.Count == 0) description = "Nothing here...";

        interaction.ReplyEmbed(new CustomEmbed {
            Title = $"{interaction.Guild.Name} - Economy Leaderboard",
            ThumbnailUrl = interaction.Guild.IconUrl,
            Color = Colors.Random,
            Description = description
        });
    }
}
