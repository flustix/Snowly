using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Database;
using Snowly.Modules.Welcome.Components;
using Snowly.Utils;

namespace Snowly.Modules.Welcome.Commands;

public class WelcomeCreateCommand : IOptionSlashCommand
{
    public string Name => "create-welcome";
    public string Description => "Creates a welcome message for the server.";
    public Permissions? Permission => Permissions.ManageGuild;
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new()
        {
            Name = "channel",
            Description = "The channel to send the welcome message in.",
            Type = ApplicationCommandOptionType.Channel,
            Required = true
        },
        new()
        {
            Name = "json",
            Description = "The JSON to send as the welcome message.",
            Type = ApplicationCommandOptionType.String,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.Guild is null) return;

        var channel = interaction.GetChannel("channel")!;
        var json = interaction.GetString("json")!;

        var welcome = new WelcomeMessage
        {
            GuildId = interaction.Guild.Id,
            ChannelId = channel.Id,
            Message = json
        };

        var collection = MongoDatabase.GetCollection<WelcomeMessage>("welcome");
        collection.InsertOne(welcome);

        interaction.Reply($"Created welcome message in {channel.Mention}.");
    }
}
