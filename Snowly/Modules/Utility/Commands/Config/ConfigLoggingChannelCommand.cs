using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands.Config;

public class ConfigLoggingChannelCommand : IOptionSlashCommand
{
    public string Name => "loggingchannel";
    public string Description => "Sets the channel to send logs in.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "channel",
            Description = "The channel to send logs in.",
            Type = DiscordApplicationCommandOptionType.Channel,
            Required = false
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var config = Configs.GetGuildConfig(interaction.Guild.Id);
        var channel = interaction.GetChannel("channel");

        if (channel == null)
        {
            config.LoggingChannelID = 0;
            interaction.Reply("Logging channel cleared.", true);
            return;
        }

        if (!channel.CanMessage())
        {
            interaction.Reply("I can't send messages in that channel.", true);
            return;
        }

        config.LoggingChannelID = channel.Id;
        Configs.UpdateGuildConfig(config);

        interaction.Reply($"Logging channel set to {channel.Mention}.", true);
    }
}
