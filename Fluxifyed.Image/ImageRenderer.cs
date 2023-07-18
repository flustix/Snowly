using System.Numerics;
using Fluxifyed.Image.Drawables;

namespace Fluxifyed.Image;

public class ImageRenderer {
    private readonly List<Drawable> drawables = new();

    public string Path { get; init; } = "test.png";
    public Vector2 Size { get; init; } = new(500, 500);

    public void Add(Drawable drawable) {
        drawables.Add(drawable);
    }

    public void AddRange(IEnumerable<Drawable> drawables) {
        this.drawables.AddRange(drawables);
    }

    public void Render() {
        var image = new Image<Argb32>((int)Size.X, (int)Size.Y);

        foreach (var drawable in drawables) {
            drawable.Draw(image);
        }

        image.Save(Path);
    }
}
