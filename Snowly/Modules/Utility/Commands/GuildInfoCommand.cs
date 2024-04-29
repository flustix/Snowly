using System.Text;
using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class GuildInfoCommand : IOptionSlashCommand
{
    public string Name => "guild";
    public string Description => "Get information about the server.";
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "guild",
            Description = "The guild to get information about.",
            Type = ApplicationCommandOptionType.String,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction)
    {
        var idStr = interaction.GetString("guild");
        ulong customId = 0;

        if (idStr != null && !ulong.TryParse(idStr, out customId))
        {
            interaction.Reply("The guild ID is invalid.", true);
            return;
        }

        var id = customId == 0 ? interaction.Guild.Id : customId;

        if (!Snowly.Bot.Guilds.TryGetValue(id, out var guild))
        {
            interaction.Reply("The guild was not found.", true);
            return;
        }

        var embed = new CustomEmbed
        {
            Title = guild.Name,
            Description = guild.Id.ToString(),
            ThumbnailUrl = guild.IconUrl,
            Fields = new List<CustomEmbedField>
            {
                new(":1234: Server ID", $"{guild.Id}", true),
                new(":crown: Server Owner", guild.Owner.Mention, true),
                new(":busts_in_silhouette: Member Count", $"{guild.MemberCount}", true),
                new(":clock1: Created At", $"<t:{guild.CreationTimestamp.ToUnixTimeSeconds()}:f>", true),
                new(":books: Channels", getChannels(guild), true),
                new(":moyai: Emotes", $"{guild.Emojis.Count}", true),
                new(":scroll: Roles", $"{guild.Roles.Count}", true),
            }
        };

        interaction.ReplyEmbed(embed);
    }

    private static string getChannels(DiscordGuild guild)
    {
        var text = 0;
        var voice = 0;

        foreach (var channel in guild.Channels.Values)
        {
            if (channel.Type is ChannelType.Category
                or ChannelType.PublicThread
                or ChannelType.PrivateThread)
                continue;

            if (channel.Type is ChannelType.Voice or ChannelType.Stage)
                voice++;
            else
                text++;
        }

        var builder = new StringBuilder();
        builder.AppendLine($"Total: {text + voice}");
        builder.AppendLine($":pencil: Text: {text}");
        builder.AppendLine($":sound: Voice: {voice}");
        return builder.ToString();
    }
}
