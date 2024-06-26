﻿using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;

namespace Snowly.Renderer.Drawables.Sprites;

public class Sprite : Drawable
{
    public string Path { get; init; } = string.Empty;

    public override Image<Rgba32> Draw()
    {
        try
        {
            var client = new HttpClient();
            var stream = client.GetStreamAsync(Path).Result;
            var image = Image.Load<Rgba32>(stream);
            image.Mutate(x => x.Resize(Width, Height));
            return image;
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return new Image<Rgba32>(Width, Height);
        }
    }
}
