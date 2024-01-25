namespace Snowly.Image.Drawables;

public abstract class Drawable
{
    public int X { get; init; }
    public int Y { get; init; }
    public int Width { get; init; } = 1;
    public int Height { get; init; } = 1;
    public float Alpha { get; init; } = 1;
    public Rgba32 Color { get; set; } = new(255, 255, 255);

    public abstract Image<Rgba32> Draw();
}
