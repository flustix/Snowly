using NLua;

namespace Snowly.Scripting.Models.Channels.Messages.Expressions;

public class LuaSticker : ILuaSnowflake
{
    [LuaMember(Name = "id")]
    public ulong ID { get; set; }
}
