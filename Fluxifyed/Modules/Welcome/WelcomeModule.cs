using DSharpPlus.EventArgs;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Database;
using Fluxifyed.Modules.Welcome.Components;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace Fluxifyed.Modules.Welcome;

public class WelcomeModule : IModule {
    public string Name => "Welcome";
    public string Description => "Welcome new members to your server!";
    public List<ISlashCommand> SlashCommands => new();

    public Task OnMemberJoined(GuildMemberAddEventArgs args) {
        Fluxifyed.Logger.LogDebug($"User {args.Member.GetUsername()} joined the server!");

        try {
            RealmAccess.Run(realm => {
                var message = realm.All<WelcomeMessage>().FirstOrDefault(x => x.GuildId == args.Guild.Id.ToString());
                if (message is null) return;

                var channel = args.Guild.GetChannel(ulong.Parse(message.ChannelId));
                if (channel is null) return;

                var roles = message.Roles.Split(" ");
                var roleList = roles.Select(role => args.Guild.GetRole(ulong.Parse(role))).Where(roleToAdd => roleToAdd is not null).ToList();

                var content = message.Message.Replace("{user.id}", $"{args.Member.Id}")
                    .Replace("{user.mention}", $"{args.Member.Mention}")
                    .Replace("{user.name}", $"{args.Member.DisplayName}")
                    .Replace("{user.avatar}", $"{args.Member.AvatarUrl}");

                var parsed = JsonConvert.DeserializeObject<CustomMessage>(content);
                channel.SendMessageAsync(parsed.Content, parsed.ToEmbed());

                roleList.ForEach(r => args.Member.GrantRoleAsync(r, "Auto-Role"));
            });
        }
        catch (Exception e) {
            Fluxifyed.Logger.LogError(e, "Error while welcoming new member!");
        }

        return Task.CompletedTask;
    }
}
