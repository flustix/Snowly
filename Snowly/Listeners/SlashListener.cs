﻿using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using Midori.Logging;
using Snowly.Commands;
using Snowly.Constants;

namespace Snowly.Listeners;

public static class SlashListener
{
    public static async Task OnInteraction(DiscordClient sender, InteractionCreateEventArgs args)
    {
        var command = Snowly.SlashCommands.FirstOrDefault(x => x.Name == args.Interaction.Data.Name);

        try
        {
            switch (args.Interaction.Type)
            {
                case DiscordInteractionType.ApplicationCommand:
                    await onSlashCommand(command, args);
                    break;

                case DiscordInteractionType.AutoComplete:
                    await onAutoComplete(command, args);
                    break;
            }
        }
        catch (Exception e)
        {
            Logger.Error(e, $"An error occurred while executing command {command?.Name ?? "*undefined*"}.");

            await args.Interaction.CreateResponseAsync(DiscordInteractionResponseType.ChannelMessageWithSource,
                new DiscordInteractionResponseBuilder().AddEmbed(new DiscordEmbedBuilder
                {
                    Title = "An error occurred",
                    Description = "An error occurred while executing this command.",
                    Color = Colors.Error,
                    ImageUrl = getRandomErrorGif()
                }));
        }
    }

    private static async Task onSlashCommand(ISlashCommand command, InteractionCreateEventArgs args)
    {
        if (command == null)
        {
            await notFound(args.Interaction);
            return;
        }

        command.Handle(args.Interaction);
    }

    private static Task onAutoComplete(ISlashCommand command, InteractionCreateEventArgs args)
    {
        if (command == null)
            return Task.CompletedTask;

        DiscordInteractionDataOption focused = null;

        if (args.Interaction.Data.Options != null)
        {
            foreach (var option in args.Interaction.Data.Options)
            {
                if (option.Focused)
                    focused = option;

                if (option.Options == null) continue;

                // TODO: garbage code
                foreach (var subOption in option.Options)
                {
                    if (subOption.Focused)
                    {
                        focused = subOption;
                        break;
                    }

                    if (subOption.Options == null) continue;

                    foreach (var subSubOption in subOption.Options)
                    {
                        if (!subSubOption.Focused) continue;

                        focused = subSubOption;
                        break;
                    }
                }

                if (focused == null) continue;

                command.HandleAutoComplete(args.Interaction, focused);
            }
        }

        return Task.CompletedTask;
    }

    private static async Task notFound(DiscordInteraction interaction)
    {
        await interaction.CreateResponseAsync(DiscordInteractionResponseType.ChannelMessageWithSource, new DiscordInteractionResponseBuilder().AddEmbed(new DiscordEmbedBuilder
        {
            Title = "Unknown command",
            Description = "This command is not implemented yet.",
            Color = Colors.Warning,
            ImageUrl = getRandomErrorGif()
        }));
    }

    private static readonly List<string> gifs = new()
    {
        "https://media.discordapp.net/attachments/328453138665439232/1066616268406407168/gyFdA6F.gif",
        "https://media.discordapp.net/attachments/1080605262559322112/1082567997316673656/1946004A-73E7-45E9-B687-960F3ACC53BA.gif",
        "https://media.discordapp.net/attachments/937487144933539881/986090958057775144/globe.gif"
    };

    private static string getRandomErrorGif()
    {
        var random = new Random();
        var number = random.Next(0, gifs.Count);
        return gifs[number];
    }
}
