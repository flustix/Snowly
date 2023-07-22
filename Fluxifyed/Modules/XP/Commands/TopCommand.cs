using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
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
            var all = XpUtils.GetTopUsers(realm, interaction.Guild.Id.ToString());

            int pages = (int) Math.Ceiling(all.Count / 10d);
            var users = all.Take(10).ToList();

            var response = new DiscordInteractionResponseBuilder();
            response.AddEmbed(new CustomEmbed
            {
                Title = $"{interaction.Guild.Name} - XP Leaderboard",
                ThumbnailUrl = interaction.Guild.IconUrl,
                Color = Colors.Random,
                Footer = new CustomEmbedFooter
                {
                    Text = $"Page 1/{pages} | {users.Count} users"
                },
                Description = string.Join("\n",
                    users.Select((user, index) => $"#{index + 1} <@{user.UserId}> - {user.Xp} XP | Level {user.Level}"))
            }.Build());

            if (pages > 1)
                response.AddComponents(new DiscordButtonComponent(ButtonStyle.Primary, "xp-top-0", "<", true), new DiscordButtonComponent(ButtonStyle.Primary, "xp-top-2", ">", pages == 1));

            interaction.CreateResponseAsync(InteractionResponseType.ChannelMessageWithSource, response);
        });
    }

    public void HandleButton(ComponentInteractionCreateEventArgs args)
    {
        RealmAccess.Run(realm =>
        {
            var users = XpUtils.GetTopUsers(realm, args.Guild.Id.ToString());
            var page = int.Parse(args.Id.Split('-')[2]) - 1;

            int pages = (int) Math.Ceiling(users.Count / 10d);
            var usersPage = users.Skip(page * 10).Take(10).ToList();

            var response = new DiscordInteractionResponseBuilder();
            response.AddEmbed(new CustomEmbed
            {
                Title = $"{args.Guild.Name} - XP Leaderboard",
                ThumbnailUrl = args.Guild.IconUrl,
                Color = Colors.Random,
                Footer = new CustomEmbedFooter
                {
                    Text = $"Page {page + 1}/{pages} | {users.Count} users"
                },
                Description = string.Join("\n",
                    usersPage.Select((user, index) => $"#{index + 1} <@{user.UserId}> - {user.Xp} XP | Level {user.Level}"))
            }.Build());
            response.AddComponents(new DiscordButtonComponent(ButtonStyle.Primary, $"xp-top-{page - 1}", "<", page == 1), new DiscordButtonComponent(ButtonStyle.Primary, $"xp-top-{page + 1}", ">", page == pages));

            args.Interaction.CreateResponseAsync(InteractionResponseType.UpdateMessage, response);
        });
    }
}
