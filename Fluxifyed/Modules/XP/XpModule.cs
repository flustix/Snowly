using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Config;
using Fluxifyed.Constants;
using Fluxifyed.Modules.XP.Commands;
using Fluxifyed.Modules.XP.Components;
using Fluxifyed.Modules.XP.Utils;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;

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

    public async Task OnComponentInteraction(ComponentInteractionCreateEventArgs args)
    {
        if (args.Id.StartsWith("xp-top")) TopCommand.HandleButton(args);

        await Task.CompletedTask;
    }

    public async Task OnMessageReceived(MessageCreateEventArgs args) {
        if (args.Guild is null) return;
        if (args.Channel.IsPrivate) return;
        if (args.Author is not DiscordMember member) return;
        if (args.Author.IsBot) return;
        var guildConfig = Configs.GetGuildConfig(args.Guild.Id);
        var xpEnabled = guildConfig?.XpEnabled ?? true;

        if (!xpEnabled) return;

        var user = XpUtils.GetUser(args.Guild.Id, args.Author.Id);

        if (user.LastMessage + 60 > DateTimeOffset.Now.ToUnixTimeSeconds()) return;

        var level = user.Level;
        var mulitplierRoles = XpUtils.GetMultiplierRoles(args.Guild.Id).Where(x => member.Roles.Any(r => r.Id == x.RoleId));
        var mulitplier = 1d + mulitplierRoles.Sum(role => role.Multiplier);
        var channelMultiplier = XpUtils.GetMultiplierChannels(args.Guild.Id).FirstOrDefault(c => c.ChannelId == args.Channel.Id);

        if (channelMultiplier is not null) {
            mulitplier *= channelMultiplier.Multiplier;
        }

        var toAdd = (int) (new Random().Next(10, 20) * mulitplier);
        Fluxifyed.Logger.LogDebug($"Adding {toAdd} XP to {args.Author.GetNickname()} ({args.Author.Id})");
        user.Xp += toAdd;
        user.LastMessage = DateTimeOffset.Now.ToUnixTimeSeconds();

        XpUtils.UpdateUser(user);

        var userConfig = Configs.GetUserConfig(args.Author.Id);
        var levelUpMessages = userConfig?.LevelUpMessages ?? true;
        levelUpMessages = levelUpMessages && (guildConfig?.LevelUpMessages ?? true);

        var messageChannel = args.Channel;

        var levelUpChannel = args.Guild.Channels.FirstOrDefault(x => x.Key == guildConfig.LevelUpChannelId);

        if (levelUpChannel.Value != null && levelUpChannel.Value.CanMessage()) {
            messageChannel = levelUpChannel.Value;
            levelUpMessages = true;
        }

        if (level != user.Level && levelUpMessages) {
            await messageChannel.SendMessageAsync(new CustomEmbed
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

        handleRoles(user, member, args.Guild);

        await Task.CompletedTask;
    }

    private static void handleRoles(XpUser user, DiscordMember member, DiscordGuild guild) {
        var roles = XpUtils.GetRewardRoles(guild.Id).OrderBy(x => x.Level).ToList();
        if (!roles.Any()) return;

        var rolesToAdd = roles.Where(x => x.Level <= user.Level);

        foreach (var role in rolesToAdd) {
            var role1 = guild.Roles.FirstOrDefault(x => x.Value.Id == role.RoleId).Value;
            if (role1 is null) continue;

            if (member.Roles.Any(x => x.Id == role1.Id)) continue;

            member.GrantRoleAsync(role1, "Level up");
        }

        var rolesToRemove = roles.Where(x => x.Level > user.Level);

        foreach (var role in rolesToRemove) {
            var role1 = guild.Roles.FirstOrDefault(x => x.Value.Id == role.RoleId).Value;
            if (role1 is null) continue;

            if (member.Roles.All(x => x.Id != role1.Id)) continue;

            member.RevokeRoleAsync(role1, "Level down");
        }
    }
}
