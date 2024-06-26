﻿using DSharpPlus;
using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class AvatarCommand : IOptionSlashCommand
{
    public string Name => "avatar";
    public string Description => "Get a user's avatar";
    public bool AllowInDM => true;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "user",
            Description = "The user to get the avatar of",
            Type = DiscordApplicationCommandOptionType.User,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction)
    {
        var user = await interaction.GetUser("user") ?? interaction.User;

        var member = interaction.Guild == null ? null : await interaction.Guild.GetMemberAsync(user.Id);

        interaction.ReplyEmbed(new CustomEmbed
            {
                Title = "Avatar",
                Color = Colors.Random,
                ImageUrl = member?.GetGuildAvatarUrl(ImageFormat.Auto, 4096) ?? user.GetAvatarUrl(ImageFormat.Auto, 4096)
            }
        );
    }
}
