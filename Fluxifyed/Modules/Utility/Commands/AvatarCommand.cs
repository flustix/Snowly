using DSharpPlus;
using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Components.Message;
using Fluxifyed.Constants;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.Utility.Commands;

public class AvatarCommand : IOptionSlashCommand {
    public string Name => "avatar";
    public string Description => "Get a user's avatar";

    public List<SlashOption> Options => new() {
        new SlashOption {
            Name = "user",
            Description = "The user to get the avatar of",
            Type = ApplicationCommandOptionType.User,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction) {
        var user = await interaction.GetUser("user") ?? interaction.User;

        interaction.ReplyEmbed(new CustomEmbed {
                Title = "Avatar",
                Color = Colors.Random,
                ImageUrl = user.GetAvatarUrl(ImageFormat.Auto)
            }
        );
    }
}
