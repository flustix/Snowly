using DSharpPlus.Entities;

namespace Fluxifyed.Utils; 

public static class UserUtils {
    public static string GetUsername(this DiscordUser user) {
        return user.Discriminator switch {
            "0" => user.Username,
            "0000" => user.Username,
            _ => $"{user.Username}#{user.Discriminator}"
        };
    }
    
    public static string GetNickname(this DiscordUser user) {
        return user is DiscordMember member
            ? member.Nickname ?? member.GetUsername()
            : user.GetUsername();
    }
}