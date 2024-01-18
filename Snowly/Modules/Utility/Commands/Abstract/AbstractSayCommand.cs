using DSharpPlus;
using DSharpPlus.Entities;
using Microsoft.Extensions.Logging;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Config;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands.Abstract;

public abstract class AbstractSayCommand : IOptionSlashCommand
{
    public abstract string Name { get; }
    public abstract string Description { get; }
    public Permissions? Permission => Permissions.ManageMessages;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = OptionName,
            Description = OptionDescription,
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption
        {
            Name = "channel",
            Description = "The channel to send the message in.",
            Type = ApplicationCommandOptionType.Channel,
            Required = false
        },
        new SlashOption
        {
            Name = "reply",
            Description = "The message to reply to.",
            Type = ApplicationCommandOptionType.String,
            Required = false
        }
    };

    protected abstract string OptionName { get; }
    protected abstract string OptionDescription { get; }
    protected abstract DiscordMessageBuilder CreateMessage(string content);

    public async void Handle(DiscordInteraction interaction)
    {
        try
        {
            var guildConfig = Configs.GetGuildConfig(interaction.Guild.Id);
            var content = interaction.GetString(OptionName);
            var channel = interaction.GetChannel("channel") ?? interaction.Channel;
            var replyString = interaction.GetString("reply");

            if (content == null || string.IsNullOrWhiteSpace(content))
            {
                interaction.Reply("You must provide a message to send.", true);
                return;
            }

            if (!channel.CanMessage())
            {
                interaction.Reply("I can't send messages in that channel.", true);
                return;
            }

            var msg = CreateMessage(content);

            if (replyString != null)
            {
                if (!ulong.TryParse(replyString, out var reply))
                {
                    interaction.Reply("You must provide a valid message ID to reply to.", true);
                    return;
                }

                var messageToReplyTo = await channel.GetMessageAsync(reply);

                if (messageToReplyTo == null)
                {
                    interaction.Reply("I couldn't find that message.", true);
                    return;
                }

                msg = msg.WithReply(reply, true);
            }

            var message = await channel.SendMessageAsync(msg);
            interaction.Reply("Message sent!", true);

            var loggingChannel = interaction.Guild.GetChannel(guildConfig.LoggingChannelID);

            if (loggingChannel != null)
            {
                var embed = new DiscordEmbedBuilder()
                            .WithAuthor(interaction.User.GetUsername(), $"https://discord.com/users/{interaction.User.Id}", iconUrl: interaction.User.AvatarUrl)
                            .WithDescription($"**Message sent in {channel.Mention}**")
                            .AddField("Content", content, true)
                            .WithColor(Colors.Random);

                embed.AddField("Message", message.JumpLink.ToString(), true);

                await loggingChannel.SendMessageAsync(embed);
            }
            else
            {
                Snowly.Logger.LogWarning($"[{guildConfig.ID}] Logging channel {guildConfig.LoggingChannelID} not found.");
            }
        }
        catch (Exception e)
        {
            var error = new CustomEmbed
            {
                Title = "An error occurred while executing this command:",
                Description = e.Message,
                Color = DiscordColor.Red
            };

            if (e.StackTrace != null)
            {
                var stackTrace = e.StackTrace.Split("\n");
                var stackTraceString = stackTrace.Where((_, i) => i != 0).Aggregate("", (current, t) => current + t + "\n");

                // limit the stack trace to 1024 characters
                if (stackTraceString.Length > 1014)
                {
                    stackTraceString = stackTraceString[..1014];
                }

                error.AddField("Stack Trace", $"```cs\n{stackTraceString}```");
            }

            interaction.ReplyEmbed(error, true);
        }
    }
}
