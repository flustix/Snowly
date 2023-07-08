using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Utility.Commands; 

public class AboutCommand : ISlashCommand {
    public string Name => "about";
    public string Description => "About Fluxifyed";
    
    public void Handle(DiscordInteraction interaction) {
        interaction.ReplyEmbed(new CustomEmbed {
            Title = "About Fluxifyed",
            Description = "A powerful and feature-rich Discord bot (soon) with a lot of customization.",
            ThumbnailUrl = Fluxifyed.Bot.CurrentUser.GetAvatarUrl(ImageFormat.Auto),
            Color = Colors.Accent,
            Fields = new List<CustomEmbedField> {
                new() {
                    Name = ":bust_in_silhouette: Author",
                    Value = "Flustix#5433",
                    Inline = true
                },
                new() {
                    Name = ":scroll: Source Code",
                    Value = "[GitHub](https://github.com/Fluxifyed/Fluxifyed)",
                    Inline = true
                }
            }
        });
    }
}