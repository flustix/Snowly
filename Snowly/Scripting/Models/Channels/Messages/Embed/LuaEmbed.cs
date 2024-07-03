using JetBrains.Annotations;
using NLua;
using Snowly.Components.Message;

namespace Snowly.Scripting.Models.Channels.Messages.Embed;

[UsedImplicitly(ImplicitUseTargetFlags.WithMembers)]
public class LuaEmbed : ILuaModel
{
    [LuaMember(Name = "title")]
    public string Title { get; set; }

    [LuaHide]
    public CustomEmbed ToCustomEmbed()
    {
        var embed = new CustomEmbed();

        if (!string.IsNullOrWhiteSpace(Title))
            embed.Title = Title;

        return embed;
    }
}
