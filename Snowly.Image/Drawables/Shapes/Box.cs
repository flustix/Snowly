using SixLabors.ImageSharp.Drawing.Processing;

namespace Snowly.Image.Drawables.Shapes;

public class Box : Drawable
{
    public override Image<Rgba32> Draw()
    {
        var image = new Image<Rgba32>(Width, Height);
        image.Mutate(x => x.Fill(Color));
        return image;
    }
}
