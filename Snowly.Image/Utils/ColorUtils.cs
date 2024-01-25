using System.Numerics;

namespace Snowly.Image.Utils;

public static class ColorUtils
{
    public static Color White => new(new Rgba32(255, 255, 255));

    private const double tolerance = 0.0001d;

    public static Vector3 ToHsl(this Rgba32 rgba32)
    {
        var r = rgba32.R / 255f;
        var g = rgba32.G / 255f;
        var b = rgba32.B / 255f;

        var min = Math.Min(Math.Min(r, g), b);
        var max = Math.Max(Math.Max(r, g), b);
        var delta = max - min;

        var h = 0f;
        var s = 0f;
        var l = (max + min) / 2.0f;

        if (delta == 0) return new Vector3(h, s, l);

        if (l < 0.5f)
            s = delta / (max + min);
        else
            s = delta / (2.0f - max - min);

        if (Math.Abs(r - max) < tolerance)
            h = (g - b) / delta;
        else if (Math.Abs(g - max) < tolerance)
            h = 2f + (b - r) / delta;
        else if (Math.Abs(b - max) < tolerance) h = 4f + (r - g) / delta;

        return new Vector3(h, s, l);
    }

    public static Rgba32 Blend(this Rgba32 color, Rgba32 other, float alpha)
    {
        var r = (byte)(color.R * (1 - alpha) + other.R * alpha);
        var g = (byte)(color.G * (1 - alpha) + other.G * alpha);
        var b = (byte)(color.B * (1 - alpha) + other.B * alpha);

        return new Rgba32(r, g, b);
    }
}
