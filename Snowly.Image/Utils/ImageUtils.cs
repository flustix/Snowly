using SixLabors.Fonts;
using SixLabors.ImageSharp.Drawing;
using SixLabors.ImageSharp.Drawing.Processing;
using SixLabors.ImageSharp.Processing.Processors.Quantization;

namespace Snowly.Image.Utils;

public static class ImageUtils
{
    public static Image<Rgba32> ApplyRoundedCorners(this Image<Rgba32> img, float radius)
    {
        if (radius <= 0) return img;

        var rect = new Rectangle(0, 0, img.Width, img.Height);
        var path = rect.getPath((int)radius);

        var mask = new Image<Rgba32>(img.Width, img.Height);
        mask.Mutate(x => x.Fill(ColorUtils.White, path));
        mask.Mutate(x => x.DrawImage(img, PixelColorBlendingMode.Multiply, PixelAlphaCompositionMode.SrcIn, 1));

        return mask;
    }

    private static IPath getPath(this Rectangle rect, int radius)
    {
        var builder = new PathBuilder();
        builder.AddLine(rect.Left + radius, rect.Top, rect.Right - radius, rect.Top);
        builder.AddArc(new Point(rect.Right - radius, rect.Top + radius), radius, radius, 0, 270, 90);
        builder.AddLine(rect.Right, rect.Top + radius, rect.Right, rect.Bottom - radius);
        builder.AddArc(new Point(rect.Right - radius, rect.Bottom - radius), radius, radius, 0, 0, 90);
        builder.AddLine(rect.Right - radius, rect.Bottom, rect.Left + radius, rect.Bottom);
        builder.AddArc(new Point(rect.Left + radius, rect.Bottom - radius), radius, radius, 0, 90, 90);
        builder.AddLine(rect.Left, rect.Bottom - radius, rect.Left, rect.Top + radius);
        builder.AddArc(new Point(rect.Left + radius, rect.Top + radius), radius, radius, 0, 180, 90);
        builder.CloseFigure();
        return builder.Build();
    }

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
