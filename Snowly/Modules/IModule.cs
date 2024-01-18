using DSharpPlus.EventArgs;
using Snowly.Commands;

namespace Snowly.Modules;

public interface IModule
{
    string Name { get; }
    string Description { get; }
    List<ISlashCommand> SlashCommands { get; }

    // Channels
    public Task OnChannelCreated(ChannelCreateEventArgs args) => Task.CompletedTask;
    public Task OnChannelDeleted(ChannelDeleteEventArgs args) => Task.CompletedTask;
    public Task OnChannelUpdated(ChannelUpdateEventArgs args) => Task.CompletedTask;

    // Messages
    public Task OnMessageReceived(MessageCreateEventArgs args) => Task.CompletedTask;
    public Task OnMessageDeleted(MessageDeleteEventArgs args) => Task.CompletedTask;
    public Task OnMessagesBulkDeleted(MessageBulkDeleteEventArgs args) => Task.CompletedTask;
    public Task OnMessageUpdated(MessageUpdateEventArgs args) => Task.CompletedTask;

    // Reactions
    public Task OnReactionAdded(MessageReactionAddEventArgs args) => Task.CompletedTask;
    public Task OnReactionRemoved(MessageReactionRemoveEventArgs args) => Task.CompletedTask;
    public Task OnReactionsCleared(MessageReactionsClearEventArgs args) => Task.CompletedTask;

    // Roles
    public Task OnRoleCreated(GuildRoleCreateEventArgs args) => Task.CompletedTask;
    public Task OnRoleDeleted(GuildRoleDeleteEventArgs args) => Task.CompletedTask;
    public Task OnRoleUpdated(GuildRoleUpdateEventArgs args) => Task.CompletedTask;

    // Guilds
    public Task OnGuildAdded(GuildCreateEventArgs args) => Task.CompletedTask;
    public Task OnGuildRemoved(GuildDeleteEventArgs args) => Task.CompletedTask;
    public Task OnGuildAvailable(GuildCreateEventArgs args) => Task.CompletedTask;
    public Task OnGuildUnavailable(GuildDeleteEventArgs args) => Task.CompletedTask;
    public Task OnGuildUpdated(GuildUpdateEventArgs args) => Task.CompletedTask;

    // Integrations
    public Task OnIntegrationCreated(IntegrationCreateEventArgs args) => Task.CompletedTask;
    public Task OnIntegrationUpdated(IntegrationUpdateEventArgs args) => Task.CompletedTask;
    public Task OnIntegrationDeleted(IntegrationDeleteEventArgs args) => Task.CompletedTask;

    // Members
    public Task OnMemberJoined(GuildMemberAddEventArgs args) => Task.CompletedTask;
    public Task OnMemberLeft(GuildMemberRemoveEventArgs args) => Task.CompletedTask;
    public Task OnMemberBanned(GuildBanAddEventArgs args) => Task.CompletedTask;
    public Task OnMemberUnbanned(GuildBanRemoveEventArgs args) => Task.CompletedTask;
    public Task OnMemberUpdated(GuildMemberUpdateEventArgs args) => Task.CompletedTask;

    // Users
    public Task OnUserUpdated(UserUpdateEventArgs args) => Task.CompletedTask;
    public Task OnUserTyping(TypingStartEventArgs args) => Task.CompletedTask;

    // Invite
    public Task OnInviteCreated(InviteCreateEventArgs args) => Task.CompletedTask;
    public Task OnInviteDeleted(InviteDeleteEventArgs args) => Task.CompletedTask;

    // Interactions
    public Task OnComponentInteraction(ComponentInteractionCreateEventArgs args) => Task.CompletedTask;
    public Task OnModal(ModalSubmitEventArgs args) => Task.CompletedTask;

    // Threads
    public Task OnThreadCreated(ThreadCreateEventArgs args) => Task.CompletedTask;
    public Task OnThreadUpdated(ThreadUpdateEventArgs args) => Task.CompletedTask;
    public Task OnThreadDeleted(ThreadDeleteEventArgs args) => Task.CompletedTask;
    public Task OnThreadMemberUpdated(ThreadMemberUpdateEventArgs args) => Task.CompletedTask;

    // Stages
    public Task OnStageStarted(StageInstanceCreateEventArgs args) => Task.CompletedTask;
    public Task OnStageEnded(StageInstanceDeleteEventArgs args) => Task.CompletedTask;
    public Task OnStageUpdated(StageInstanceUpdateEventArgs args) => Task.CompletedTask;
}
