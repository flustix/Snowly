using System.Reflection;

namespace Snowly.Renderer.Utils;

public static class AssetUtils
{
    public static Stream? ReadFromAssembly(string name)
    {
        var assembly = Assembly.GetExecutingAssembly();
        var resource = $"{assembly.GetName().Name}.{name.Replace("/", ".")}";
        return assembly.GetManifestResourceStream(resource);
    }
}
