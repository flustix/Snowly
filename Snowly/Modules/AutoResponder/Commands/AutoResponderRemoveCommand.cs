using DSharpPlus.Entities;
using MongoDB.Driver;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.AutoResponder.Commands;

public class AutoResponderRemoveCommand : IOptionSlashCommand
{
    public string Name => "remove";
    public string Description => "Remove an auto-response.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "trigger",
            Description = "The message that will trigger the response.",
            Type = DiscordApplicationCommandOptionType.String,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var trigger = interaction.GetString("trigger");

        if (trigger is null)
        {
            interaction.Reply("Invalid arguments.", true);
            return;
        }

        var responses = AutoResponderModule.Responses.Find(x => x.GuildID == interaction.Guild.Id && x.Trigger == trigger).ToList();

        if (responses.Count == 0)
        {
            interaction.Reply($"An auto-response for `{trigger}` does not exist.", true);
            return;
        }

        AutoResponderModule.Responses.DeleteMany(x => x.GuildID == interaction.Guild.Id && x.Trigger == trigger);

        interaction.Reply($"Removed auto-response for `{trigger}`.", true);

        var config = Configs.GetGuildConfig(interaction.Guild.Id);
        var loggingChannel = interaction.Guild.GetChannel(config.LoggingChannelID);

        if (loggingChannel != null)
        {
            var embed = new DiscordEmbedBuilder()
                        .WithAuthor(interaction.User.GetUsername(), iconUrl: interaction.User.AvatarUrl)
                        .WithDescription("Auto-response removed")
                        .AddField("Trigger", trigger, true)
                        .WithColor(Colors.Red);

            loggingChannel.SendMessageAsync(embed);
        }
    }
}
