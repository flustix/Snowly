namespace Fluxifyed.Image.Drawables.Shapes;

public class Box : Drawable {
    public override void Draw(Image<Argb32> image) {
        const double pi = 3.14159265358979323846264338327950288419716939937510;

        for (var x = 0; x < Width; x++) {
            for (var y = 0; y < Height; y++) {
                // draw rounded corners
                if (CornerRadius > 0) {
                    // draw circles in each corner
                    if (x < CornerRadius && y < CornerRadius) {
                        // top left
                        var angle = Math.Atan2(CornerRadius - y, CornerRadius - x);
                        var distance = Math.Sqrt(Math.Pow(CornerRadius - x, 2) + Math.Pow(CornerRadius - y, 2));

                        if (angle < pi / 2 && distance < CornerRadius) {
                            image[X + x, Y + y] = Color;
                        }
                    }
                    else if (x < CornerRadius && y > Height - CornerRadius) {
                        // bottom left
                        var angle = Math.Atan2(y - (Height - CornerRadius), CornerRadius - x);
                        var distance = Math.Sqrt(Math.Pow(CornerRadius - x, 2) + Math.Pow(y - (Height - CornerRadius), 2));

                        if (angle < pi / 2 && distance < CornerRadius) {
                            image[X + x, Y + y] = Color;
                        }
                    }
                    else if (x > Width - CornerRadius && y < CornerRadius) {
                        // top right
                        var angle = Math.Atan2(CornerRadius - y, x - (Width - CornerRadius));
                        var distance = Math.Sqrt(Math.Pow(x - (Width - CornerRadius), 2) + Math.Pow(CornerRadius - y, 2));

                        if (angle < pi / 2 && distance < CornerRadius) {
                            image[X + x, Y + y] = Color;
                        }
                    }
                    else if (x > Width - CornerRadius && y > Height - CornerRadius) {
                        // bottom right
                        var angle = Math.Atan2(y - (Height - CornerRadius), x - (Width - CornerRadius));
                        var distance = Math.Sqrt(Math.Pow(x - (Width - CornerRadius), 2) + Math.Pow(y - (Height - CornerRadius), 2));

                        if (angle < pi / 2 && distance < CornerRadius) {
                            image[X + x, Y + y] = Color;
                        }
                    }
                    else {
                        image[X + x, Y + y] = Color;
                    }
                }
                else {
                    image[X + x, Y + y] = Color;
                }
            }
        }
    }
}
