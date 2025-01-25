﻿using DSharpPlus.Entities;
using Midori.Logging;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using Snowly.Commands;
using Snowly.Renderer.Utils;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class AccentColorCommand : IOptionSlashCommand
{
    public string Name => "accentcolor";
    public string Description => "Gets 5 accent colors from an image.";
    public bool AllowInDM => true;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "attachment",
            Description = "The image to get the accent colors from.",
            Type = DiscordApplicationCommandOptionType.Attachment,
            Required = true
        }
    };

    public async void Handle(DiscordInteraction interaction)
    {
        try
        {
            await interaction.Acknowledge();

            var attachment = interaction.GetAttachment("attachment");

            if (attachment is null)
            {
                interaction.Followup("No attachment found.", true);
                return;
            }

            var type = attachment.MediaType;

            if (type != "image/png" && type != "image/jpeg")
            {
                interaction.Followup("The attachment must be a png or jpeg image.", true);
                return;
            }

            // save the attachment to a stream
            var http = new HttpClient();
            var response = await http.GetAsync(attachment.Url);
            var stream = await response.Content.ReadAsStreamAsync();

            // get the accent colors
            var image = await Image.LoadAsync<Rgba32>(stream);
            var colors = image.GetAccentColors();
            var palette = ImageUtils.RenderColorPalette(colors);

            var fs = new FileStream("palette.png", FileMode.Create);
            await palette.SaveAsPngAsync(fs);
            fs.Close();

            fs = new FileStream("palette.png", FileMode.Open);

            var msg = new DiscordMessageBuilder().AddFile(fs);
            await interaction.CreateFollowupMessageAsync(new DiscordFollowupMessageBuilder(msg));
            fs.Close();
        }
        catch (Exception e)
        {
            Logger.Error(e, "Error getting accent colors.");
            interaction.Followup("Error getting accent colors.", true);
        }
        finally
        {
            // delete the palette image
            if (File.Exists("palette.png")) File.Delete("palette.png");
        }
    }
}
