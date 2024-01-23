using DSharpPlus;
using DSharpPlus.Entities;
using MongoDB.Driver;
using Snowly.Commands;
using Snowly.Modules.AutoResponder.Components;
using Snowly.Utils;

namespace Snowly.Modules.AutoResponder.Commands;

public class AutoResponderAddCommand : IOptionSlashCommand
{
    public string Name => "add";
    public string Description => "Add an auto-response.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "trigger",
            Description = "The message that will trigger the response.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption
        {
            Name = "response",
            Description = "The response to the message.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        },
        new SlashOption
        {
            Name = "channel",
            Description = "The channel to respond in. Leave blank to respond in the same channel.",
            Type = ApplicationCommandOptionType.Channel,
            Required = false
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        var trigger = interaction.GetString("trigger");
        var response = interaction.GetString("response");
        var channel = interaction.GetChannel("channel");

        if (trigger is null || response is null)
        {
            interaction.Reply("Invalid arguments.", true);
            return;
        }

        var responses = AutoResponderModule.Responses.Find(x => x.GuildID == interaction.Guild.Id && x.Trigger == trigger).ToList();

        if (responses.Count > 0)
        {
            interaction.Reply($"An auto-response for `{trigger}` already exists.", true);
            return;
        }

        var autoResponse = new AutoResponse
        {
            GuildID = interaction.Guild.Id,
            Trigger = trigger,
            Response = response,
            ChannelID = channel?.Id ?? 0
        };

        AutoResponderModule.Responses.InsertOne(autoResponse);
        interaction.Reply($"Added auto-response for `{trigger}`.", true);
    }
}
