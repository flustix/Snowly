using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Utils;
using Newtonsoft.Json;

namespace Fluxifyed.Modules.Utility.Commands;

public class SayJsonCommand : IOptionSlashCommand {
    public string Name => "say-json";
    public string Description => "Sends a json message in a channel.";
    public Permissions? Permission => Permissions.ManageMessages;

    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "json",
            Description = "The message to send. (https://flustix.foxes4life.net/fluxifyed for a generator)",
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
            var json = interaction.GetString("json");
            var channel = interaction.GetChannel("channel") ?? interaction.Channel;
            var replyString = interaction.GetString("reply");

            if (json == null || string.IsNullOrWhiteSpace(json)) {
                interaction.Reply("You must provide a message to send.", true);
                return;
            }

            if (!channel.CanMessage()) {
                interaction.Reply("I can't send messages in that channel.", true);
                return;
            }

            var message = JsonConvert.DeserializeObject<CustomMessage>(json);
            var msg = new DiscordMessageBuilder()
                .WithContent(message.Content)
                .AddEmbed(message.ToEmbed());

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

                await channel.SendMessageAsync(msg.WithReply(reply, true));
                interaction.Reply("Message sent!", true);
                return;
            }

            await channel.SendMessageAsync(msg);
            interaction.Reply("Message sent!", true);
        }
        catch (Exception e) {
            var error = new CustomEmbed {
                Title = "An error occurred while executing this command:",
                Description = e.Message,
                Color = DiscordColor.Red
            };

            if (e.StackTrace != null) {
                var stackTrace = e.StackTrace.Split("\n");
                var stackTraceString = stackTrace.Where((_, i) => i != 0).Aggregate("", (current, t) => current + t + "\n");

                error.AddField("Stack Trace", $"```cs\n{stackTraceString}```");
            }

            interaction.ReplyEmbed(error, true);
        }
    }
}
