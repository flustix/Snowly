using DSharpPlus;
using DSharpPlus.EventArgs;
using Midori.Logging;

namespace Snowly.Listeners;

public static class MultiListener
{
    public static async Task ChannelCreated(DiscordClient sender, ChannelCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnChannelCreated(args);
    }

    public static async Task ChannelDeleted(DiscordClient sender, ChannelDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnChannelDeleted(args);
    }

    public static async Task ChannelUpdated(DiscordClient sender, ChannelUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnChannelUpdated(args);
    }

    public static async Task OnMessageReceived(DiscordClient sender, MessageCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMessageReceived(args);
    }

    public static async Task OnMessageDeleted(DiscordClient sender, MessageDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMessageDeleted(args);
    }

    public static async Task OnMessageBulkDeleted(DiscordClient sender, MessageBulkDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMessagesBulkDeleted(args);
    }

    public static async Task OnMessageUpdated(DiscordClient sender, MessageUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMessageUpdated(args);
    }

    public static async Task OnReactionAdded(DiscordClient sender, MessageReactionAddEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnReactionAdded(args);
    }

    public static async Task OnReactionRemoved(DiscordClient sender, MessageReactionRemoveEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnReactionRemoved(args);
    }

    public static async Task OnReactionsCleared(DiscordClient sender, MessageReactionsClearEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnReactionsCleared(args);
    }

    public static async Task RoleCreated(DiscordClient sender, GuildRoleCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnRoleCreated(args);
    }

    public static async Task RoleDeleted(DiscordClient sender, GuildRoleDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnRoleDeleted(args);
    }

    public static async Task RoleUpdated(DiscordClient sender, GuildRoleUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnRoleUpdated(args);
    }

    public static async Task JoinedGuild(DiscordClient sender, GuildCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnGuildAdded(args);
    }

    public static async Task LeftGuild(DiscordClient sender, GuildDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnGuildRemoved(args);
    }

    public static async Task GuildAvailable(DiscordClient sender, GuildCreateEventArgs args)
    {
        Logger.Log($"Loaded guild '{args.Guild.Name}' ({args.Guild.Id})");
        foreach (var module in Snowly.Modules) await module.OnGuildAvailable(args);
    }

    public static async Task GuildUnavailable(DiscordClient sender, GuildDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnGuildUnavailable(args);
    }

    public static async Task GuildUpdated(DiscordClient sender, GuildUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnGuildUpdated(args);
    }

    public static async Task IntegrationCreated(DiscordClient sender, IntegrationCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnIntegrationCreated(args);
    }

    public static async Task IntegrationDeleted(DiscordClient sender, IntegrationDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnIntegrationDeleted(args);
    }

    public static async Task IntegrationUpdated(DiscordClient sender, IntegrationUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnIntegrationUpdated(args);
    }

    public static async Task UserJoined(DiscordClient sender, GuildMemberAddEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMemberJoined(args);
    }

    public static async Task UserLeft(DiscordClient sender, GuildMemberRemoveEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMemberLeft(args);
    }

    public static async Task UserBanned(DiscordClient sender, GuildBanAddEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMemberBanned(args);
    }

    public static async Task UserUnbanned(DiscordClient sender, GuildBanRemoveEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMemberUnbanned(args);
    }

    public static async Task GuildMemberUpdated(DiscordClient sender, GuildMemberUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnMemberUpdated(args);
    }

    public static async Task UserIsTyping(DiscordClient sender, TypingStartEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnUserTyping(args);
    }

    public static async Task UserUpdated(DiscordClient sender, UserUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnUserUpdated(args);
    }

    public static async Task InviteCreated(DiscordClient sender, InviteCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnInviteCreated(args);
    }

    public static async Task InviteDeleted(DiscordClient sender, InviteDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnInviteDeleted(args);
    }

    public static async Task ComponentInteraction(DiscordClient sender, ComponentInteractionCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnComponentInteraction(args);
    }

    public static async Task ModalSubmitted(DiscordClient sender, ModalSubmitEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnModal(args);
    }

    public static async Task ThreadCreated(DiscordClient sender, ThreadCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnThreadCreated(args);
    }

    public static async Task ThreadDeleted(DiscordClient sender, ThreadDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnThreadDeleted(args);
    }

    public static async Task ThreadUpdated(DiscordClient sender, ThreadUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnThreadUpdated(args);
    }

    public static async Task ThreadMemberUpdated(DiscordClient sender, ThreadMemberUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnThreadMemberUpdated(args);
    }

    public static async Task StageStarted(DiscordClient sender, StageInstanceCreateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnStageStarted(args);
    }

    public static async Task StageEnded(DiscordClient sender, StageInstanceDeleteEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnStageEnded(args);
    }

    public static async Task StageUpdated(DiscordClient sender, StageInstanceUpdateEventArgs args)
    {
        foreach (var module in Snowly.Modules) await module.OnStageUpdated(args);
    }
}
