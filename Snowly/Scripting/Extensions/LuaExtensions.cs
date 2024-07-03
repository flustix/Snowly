using DSharpPlus.Entities;
using Snowly.Scripting.Models;
using Snowly.Scripting.Models.Channels;

namespace Snowly.Scripting.Extensions;

public static class LuaExtensions
{
    public static LuaGuild ToLua(this DiscordGuild guild) => new(guild);
    public static LuaChannel ToLua(this DiscordChannel channel) => new(channel);
}
