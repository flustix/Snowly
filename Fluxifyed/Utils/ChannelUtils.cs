using DSharpPlus;
using DSharpPlus.Entities;

namespace Fluxifyed.Utils; 

public static class ChannelUtils {
    public static bool CanMessage(this DiscordChannel channel) {
        return channel.Type is ChannelType.Text
            or ChannelType.Private
            or ChannelType.Voice
            or ChannelType.Group
            or ChannelType.News
            or ChannelType.NewsThread
            or ChannelType.PublicThread
            or ChannelType.PrivateThread
            or ChannelType.Stage;
    }
}