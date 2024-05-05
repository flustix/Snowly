using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Drawing.Processing;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;

namespace Snowly.Renderer.Drawables.Shapes;

public class Box : Drawable
{
    public override Image<Rgba32> Draw()
    {
        var image = new Image<Rgba32>(Width, Height);
        image.Mutate(x => x.Fill(Color));
        return image;
    }
}
