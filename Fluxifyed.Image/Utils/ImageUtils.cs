﻿using System.Numerics;
using SixLabors.Fonts;
using SixLabors.ImageSharp.Drawing.Processing;
using SixLabors.ImageSharp.Processing.Processors.Dithering;
using SixLabors.ImageSharp.Processing.Processors.Quantization;

namespace Fluxifyed.Image.Utils;

public static class ImageUtils
{
    public static List<Rgba32> GetAccentColors(this Image<Rgba32> image)
    {
        var colors = new List<Rgba32>();
        image.Mutate(x => x.Quantize(new WuQuantizer(new QuantizerOptions { MaxColors = 5 })));

        for (var x = 0; x < image.Width; x++)
        {
            for (var y = 0; y < image.Height; y++)
            {
                var pixel = image[x, y];
                if (!colors.Contains(pixel)) colors.Add(pixel);
            }
        }

        return colors;
    }

    public static Image<Rgba32> RenderColorPalette(List<Rgba32> colors)
    {
        const int font_size = 18;

        var image = new Image<Rgba32>(200, 200);
        var font = FontStorage.GetFont("Renogare Soft", font_size);
        var pixelPerColor = image.Height / colors.Count;

        for (var x = 0; x < image.Width; x++)
        {
            for (var y = 0; y < image.Height; y++)
            {
                var color = colors[y / pixelPerColor];
                image[x, y] = color;
            }
        }

        foreach (var rgba32 in colors)
        {
            var hsl = rgba32.ToHsl();
            var bright = hsl.Z > 0.5f;
            var textColor = bright ? new Rgba32(0, 0, 0) : new Rgba32(1f, 1f, 1f);
            var text = $"#{rgba32.ToHex()[..6]}";

            var textWidth = TextMeasurer.MeasureAdvance(text, new TextOptions(font)).Width;
            var vec = new PointF(100 - textWidth / 2f,
                colors.IndexOf(rgba32) * pixelPerColor + pixelPerColor / 2f - font_size / 2f);

            image.Mutate(x => x.DrawText(text, font, textColor, vec));
        }

        return image;
    }
}
