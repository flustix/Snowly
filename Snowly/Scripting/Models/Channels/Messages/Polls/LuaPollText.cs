using NLua;

namespace Snowly.Scripting.Models.Channels.Messages.Polls;

public class LuaPollText : ILuaModel
{
    [LuaMember(Name = "text")]
    public string Text { get; set; }

    [LuaMember(Name = "emote")]
    public ulong Emote { get; set; }
}
