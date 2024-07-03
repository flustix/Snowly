using DSharpPlus.Entities;
using NLua;

namespace Snowly.Scripting.Models.Channels;

public class LuaChannel : ILuaSnowflake
{
    [LuaMember(Name = "id")]
    public ulong ID { get; }

    public LuaChannel(DiscordChannel channel)
    {
        ID = channel.Id;
    }
}
