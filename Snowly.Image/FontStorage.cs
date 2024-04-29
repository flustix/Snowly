using SixLabors.Fonts;
using Snowly.Image.Utils;

namespace Snowly.Image;

public static class FontStorage
{
    public static string DefaultFont { get; set; } = "Arial";

    private static readonly FontCollection collection = new();
    private static readonly Dictionary<string, FontFamily> families = new();

    public static void RegisterFont(string name, string path)
    {
        var stream = AssetUtils.ReadFromAssembly(path);

        if (stream == null)
            throw new FileNotFoundException($"Font file not found: {path}");

        var family = collection.Add(stream);
        families.Add(name, family);
    }

    public static Font GetFont(string name, float size)
    {
        if (!families.ContainsKey(name))
            return SystemFonts.CreateFont(DefaultFont, size);

        return families[name].CreateFont(size);
    }
}
