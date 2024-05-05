using DSharpPlus.Entities;

namespace Snowly.Utils;

public static class ChannelUtils
{
    public static bool CanMessage(this DiscordChannel channel)
    {
        return channel.Type is DiscordChannelType.Text
            or DiscordChannelType.Private
            or DiscordChannelType.Voice
            or DiscordChannelType.Group
            or DiscordChannelType.News
            or DiscordChannelType.NewsThread
            or DiscordChannelType.PublicThread
            or DiscordChannelType.PrivateThread
            or DiscordChannelType.Stage;
    }
}
