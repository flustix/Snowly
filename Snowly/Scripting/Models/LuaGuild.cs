using DSharpPlus.Entities;
using NLua;

namespace Snowly.Scripting.Models;

public class LuaGuild : ILuaSnowflake
{
    [LuaMember(Name = "id")]
    public ulong ID { get; }

    [LuaMember(Name = "name")]
    public string Name { get; }

    public LuaGuild(DiscordGuild guild)
    {
        ID = guild.Id;
        Name = guild.Name;
    }
}
