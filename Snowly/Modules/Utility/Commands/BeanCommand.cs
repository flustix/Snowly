﻿using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Constants;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class BeanCommand : IOptionSlashCommand
{
    public string Name => "bean";
    public string Description => "Beans a user from the server.";
    public DiscordPermissions? Permission => DiscordPermissions.BanMembers;
    public bool AllowInDM => false;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "user",
            Description = "The user to bean.",
            Type = DiscordApplicationCommandOptionType.User,
            Required = true
        },
        new SlashOption
        {
            Name = "reason",
            Description = "The reason for beaning the user.",
            Type = DiscordApplicationCommandOptionType.String,
            Required = false
        }
    };

    public async void Handle(DiscordInteraction interaction)
    {
        var user = await interaction.GetMember("user");
        var reason = interaction.GetString("reason");

        if (user == null)
        {
            interaction.Reply("User not found.", true);
            return;
        }

        var embed = new CustomEmbed
        {
            Title = ":white_check_mark: Banned User!",
            Color = Colors.Red,
            Fields = new List<CustomEmbedField>
            {
                new()
                {
                    Name = ":bust_in_silhouette: User",
                    Value = user.Mention,
                    Inline = true
                },
                new()
                {
                    Name = ":hammer: Moderator",
                    Value = interaction.User.Mention,
                    Inline = true
                },
                new()
                {
                    Name = ":scroll: Reason",
                    Value = reason ?? "No reason provided."
                }
            }
        };

        interaction.ReplyEmbed(embed);
    }
}
