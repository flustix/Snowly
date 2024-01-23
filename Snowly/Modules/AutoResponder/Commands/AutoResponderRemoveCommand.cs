using DSharpPlus;
using DSharpPlus.Entities;
using MongoDB.Driver;
using Snowly.Commands;
using Snowly.Utils;

namespace Snowly.Modules.AutoResponder.Commands;

public class AutoResponderRemoveCommand : IOptionSlashCommand
{
    public string Name => "remove";
    public string Description => "Remove an auto-response.";

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "trigger",
            Description = "The message that will trigger the response.",
            Type = ApplicationCommandOptionType.String,
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
    }
}
