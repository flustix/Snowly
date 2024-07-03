using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;
using Snowly.Renderer.Utils;

namespace Snowly.Renderer.Drawables;

public class Container : Drawable
{
    public Drawable[] Drawables { get; init; } = Array.Empty<Drawable>();
    public int CornerRadius { get; init; }

    public override Image<Rgba32> Draw()
    {
        var image = new Image<Rgba32>(Width, Height);

        if (Drawables.Length == 0) return image;
        if (Alpha == 0) return image;

        foreach (var drawable in Drawables)
        {
            var drawableImage = drawable.Draw();
            image.Mutate(x => x.DrawImage(drawableImage, new Point(drawable.X, drawable.Y), drawable.Alpha));
        }

        return image.ApplyRoundedCorners(CornerRadius);
    }
}
