using System.Numerics;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;
using Snowly.Renderer.Drawables;

namespace Snowly.Renderer;

public class ImageRenderer
{
    private readonly List<Drawable> drawables = new();

    public string Path { get; init; } = "test.png";
    public Vector2 Size { get; init; } = new(500, 500);

    public void Add(Drawable drawable)
    {
        drawables.Add(drawable);
    }

    public void AddRange(IEnumerable<Drawable> drawables)
    {
        this.drawables.AddRange(drawables);
    }

    public void Render()
    {
        var image = new Image<Rgba32>((int)Size.X, (int)Size.Y);

        foreach (var drawable in drawables)
        {
            var drawableImage = drawable.Draw();
            image.Mutate(x => x.DrawImage(drawableImage, new Point(drawable.X, drawable.Y), drawable.Alpha));
        }

        image.Save(Path);
    }
}
