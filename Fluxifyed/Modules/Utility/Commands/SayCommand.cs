using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Constants;
using Fluxifyed.Database;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;

namespace Fluxifyed.Modules.Utility.Commands;

public class SayCommand : IOptionSlashCommand {
    public string Name => "say";
    public string Description => "Sends a message in a channel.";
    public Permissions? Permission => Permissions.ManageMessages;

    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "message",
            Description = "The message to send.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption {
            Name = "channel",
            Description = "The channel to send the message in.",
            Type = ApplicationCommandOptionType.Channel,
            Required = false
        },
        new SlashOption {
            Name = "reply",
            Description = "The message to reply to.",
            Type = ApplicationCommandOptionType.String,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction) {
        try {
            var guildConfig = Configs.GetGuildConfig(interaction.Guild.Id);
            var message = interaction.GetString("message");
            var channel = interaction.GetChannel("channel") ?? interaction.Channel;
            var replyString = interaction.GetString("reply");

            if (message == null || string.IsNullOrWhiteSpace(message)) {
                interaction.Reply("You must provide a message to send.", true);
                return;
            }

            if (!channel.CanMessage()) {
                interaction.Reply("I can't send messages in that channel.", true);
                return;
            }

            if (replyString != null) {
                if (!ulong.TryParse(replyString, out var reply)) {
                    interaction.Reply("You must provide a valid message ID to reply to.", true);
                    return;
                }

                var messageToReplyTo = await channel.GetMessageAsync(reply);

                if (messageToReplyTo == null) {
                    interaction.Reply("I couldn't find that message.", true);
                    return;
                }

                var msg = new DiscordMessageBuilder().WithContent(message).WithReply(reply, true);
                await channel.SendMessageAsync(msg);

                interaction.Reply("Message sent!", true);

                return;
            }

            await channel.SendMessageAsync(message);
            interaction.Reply("Message sent!", true);

            var loggingChannel = interaction.Guild.GetChannel(guildConfig.LoggingChannelId);

            if (loggingChannel != null) {
                var embed = new DiscordEmbedBuilder()
                    .WithAuthor(interaction.User.GetUsername(), iconUrl: interaction.User.AvatarUrl)
                    .WithDescription($"**Message sent in {channel.Mention}**")
                    .AddField("Message", message)
                    .WithColor(Colors.Random);

                await loggingChannel.SendMessageAsync(embed);
            }
            else {
                Fluxifyed.Logger.LogWarning($"[{guildConfig.Id}] Logging channel {guildConfig.LoggingChannelId} not found.");
            }
        }
        catch (Exception e) {
            interaction.Reply($"An error occurred while executing this command: {e.Message}", true);
        }
    }
}
