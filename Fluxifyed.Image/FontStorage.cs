using SixLabors.Fonts;

namespace Fluxifyed.Image;

public static class FontStorage
{
    public static string DefaultFont { get; set; } = "Arial";

    private static readonly FontCollection collection = new();
    private static readonly Dictionary<string, FontFamily> families = new();

    public static void RegisterFont(string name, string path)
    {
        var family = collection.Add(path);
        families.Add(name, family);
    }

    public static Font GetFont(string name, float size)
    {
        if (!families.ContainsKey(name))
            return SystemFonts.CreateFont(DefaultFont, size);

        return families[name].CreateFont(size);
    }
}
