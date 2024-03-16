using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class AboutCommand : ISlashCommand
{
    public string Name => "about";
    public string Description => "About Snowly";
    public bool AllowInDM => true;

    public void Handle(DiscordInteraction interaction)
    {
        interaction.ReplyEmbed(new CustomEmbed
        {
            Title = "About Snowly",
            Description = "A powerful and feature-rich Discord bot (soon) with a lot of customization.",
            ThumbnailUrl = Snowly.Bot.CurrentUser.GetAvatarUrl(ImageFormat.Auto),
            Color = Colors.Accent,
            Fields = new List<CustomEmbedField>
            {
                new()
                {
                    Name = ":bust_in_silhouette: Author",
                    Value = "Flustix#5433",
                    Inline = true
                },
                new()
                {
                    Name = ":scroll: Source Code",
                    Value = "[GitHub](https://github.com/Snowly/Snowly)",
                    Inline = true
                }
            }
        });
    }
}
