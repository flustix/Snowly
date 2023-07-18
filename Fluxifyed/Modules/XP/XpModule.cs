using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Config;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Modules.XP.Commands;
using Fluxifyed.Modules.XP.Components;
using Fluxifyed.Modules.XP.Utils;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;
using Realms;

namespace Fluxifyed.Modules.XP;

public class XpModule : IModule {
    public string Name => "XP";
    public string Description => "Reward your members for chatting and being active.";

    public List<ISlashCommand> SlashCommands => new() {
        new RankCommand(),
        new TopCommand(),
        new XpCommand(),
        new CalculateCommand(),
        new ToggleLevelUpCommand()
    };

    public async Task OnMessageReceived(MessageCreateEventArgs args) {
        RealmAccess.Run(realm => {
            if (args.Guild is null) return;
            if (args.Channel.IsPrivate) return;
            if (args.Author is not DiscordMember member) return;
            if (args.Author.IsBot) return;
            var guildConfig = GuildConfig.GetOrCreate(realm, args.Guild.Id.ToString());
            var xpEnabled = guildConfig?.XpEnabled ?? true;

            if (!xpEnabled) return;

            var user = XpUtils.GetUser(realm, args.Guild.Id.ToString(), args.Author.Id.ToString());

            if (user.LastMessage + 60 > DateTimeOffset.Now.ToUnixTimeSeconds()) return;

            var level = user.Level;

            var mulitplierRoles = realm.All<XpMultiplierRole>().ToList()
                .Where(x => x.GuildId == args.Guild.Id.ToString() && member.Roles.Any(y => y.Id.ToString() == x.RoleId));

            var mulitplier = 1d + mulitplierRoles.Sum(role => role.Multiplier);

            var channelMultiplier = realm.All<XpChannelMultiplier>()
                .FirstOrDefault(x => x.GuildId == args.Guild.Id.ToString() && x.ChannelId == args.Channel.Id.ToString());

            if (channelMultiplier is not null) {
                mulitplier *= channelMultiplier.Multiplier;
            }

            var toAdd = (int) (new Random().Next(10, 20) * mulitplier);
            Fluxifyed.Logger.LogDebug($"Adding {toAdd} XP to {args.Author.GetNickname()} ({args.Author.Id})");
            user.Xp += toAdd;
            user.LastMessage = DateTimeOffset.Now.ToUnixTimeSeconds();

            var userConfig = realm.Find<UserConfig>(args.Author.Id.ToString());
            var levelUpMessages = userConfig?.LevelUpMessages ?? true;
            levelUpMessages = levelUpMessages && (guildConfig?.LevelUpMessages ?? true);

            var messageChannel = args.Channel;

            if (ulong.TryParse(guildConfig?.LevelUpChannelId ?? "0", out var channelid)) {
                var channel1 = args.Guild.Channels.FirstOrDefault(x => x.Key == channelid);

                if (channel1.Value != null && channel1.Value.CanMessage()) {
                    messageChannel = channel1.Value;
                    levelUpMessages = true;
                }
            }

            if (level != user.Level && levelUpMessages) {
                messageChannel.SendMessageAsync(new CustomEmbed
                    {
                        Author = new CustomEmbedAuthor {
                            Name = $"{args.Author.GetNickname()}",
                            IconUrl = args.Author.GetAvatarUrl(ImageFormat.Auto)
                        },
                        Color = Colors.Accent,
                        Description = $"Leveled up to level **{user.Level}**!"
                    }.Build()
                );
            }

            handleRoles(user, realm, member, args.Guild);
        });

        await Task.CompletedTask;
    }

    private static void handleRoles(XpUser user, Realm realm, DiscordMember member, DiscordGuild guild) {
        var roles = realm.All<XpRewardRole>().ToList().Where(x => x.GuildId == guild.Id.ToString()).OrderBy(x => x.Level).ToList();
        if (!roles.Any()) return;

        var rolesToAdd = roles.Where(x => x.Level <= user.Level);

        foreach (var role in rolesToAdd) {
            var role1 = guild.Roles.FirstOrDefault(x => x.Value.Id.ToString() == role.RoleId).Value;
            if (role1 is null) continue;

            if (member.Roles.Any(x => x.Id == role1.Id)) continue;

            member.GrantRoleAsync(role1, "Level up");
        }

        var rolesToRemove = roles.Where(x => x.Level > user.Level);

        foreach (var role in rolesToRemove) {
            var role1 = guild.Roles.FirstOrDefault(x => x.Value.Id.ToString() == role.RoleId).Value;
            if (role1 is null) continue;

            if (member.Roles.All(x => x.Id != role1.Id)) continue;

            member.RevokeRoleAsync(role1, "Level down");
        }
    }
}
