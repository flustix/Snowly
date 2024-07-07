using DSharpPlus.Entities;
using NLua;
using Snowly.Scripting.Models.Channels.Messages.Embed;
using Snowly.Scripting.Models.Channels.Messages.Expressions;
using Snowly.Scripting.Models.Channels.Messages.Polls;

namespace Snowly.Scripting.Models.Channels;

public class LuaChannel : ILuaSnowflake
{
    [LuaHide]
    public DiscordChannel Channel { get; }

    [LuaMember(Name = "id")]
    public ulong ID => Channel.Id;

    public LuaChannel(DiscordChannel channel)
    {
        Channel = channel;
    }

    [LuaMember(Name = "send")]
    public void SendEmbed(LuaEmbed luaEmbed)
    {
        var embed = luaEmbed.ToCustomEmbed();
        var message = new DiscordMessageBuilder().AddEmbed(embed.Build());
        sendInternal(message);
    }

    [LuaMember(Name = "send")]
    public void SendSticker(string content)
    {
        var message = new DiscordMessageBuilder().WithContent(content);
        sendInternal(message);
    }

    [LuaMember(Name = "send")]
    public void SendSticker(LuaSticker luaSticker)
    {
        var sticker = Snowly.Bot.GetStickerAsync(luaSticker.ID).GetAwaiter().GetResult();
        var message = new DiscordMessageBuilder().WithStickers(new[] { sticker });
        sendInternal(message);
    }

    [LuaMember(Name = "send")]
    public void SendEmbed(LuaPoll luaPoll)
    {
        var poll = luaPoll.Build();
        var message = new DiscordMessageBuilder().WithPoll(poll);
        sendInternal(message);
    }

    [LuaHide]
    private void sendInternal(DiscordMessageBuilder message) => Channel.SendMessageAsync(message).Wait();
}
