using System.Globalization;
using DSharpPlus.Entities;

namespace Fluxifyed.Constants; 

public static class Colors {
    public static DiscordColor Red => FromHex("#FF5555");
    public static DiscordColor Orange => FromHex("#FFAA55");
    public static DiscordColor Yellow => FromHex("#FFFF55");
    public static DiscordColor Green => FromHex("#55FF55");
    public static DiscordColor Mint => FromHex("#55FFAA");
    public static DiscordColor Cyan => FromHex("#55FFFF");
    public static DiscordColor Blue => FromHex("#5555FF");
    public static DiscordColor Purple => FromHex("#FF55FF");
    public static DiscordColor Pink => FromHex("#FF55AA");
    
    public static DiscordColor Accent => FromHex("#ef6624");
    
    public static DiscordColor Error => Red;
    public static DiscordColor Success => Green;
    public static DiscordColor Warning => FromHex("#ffaa00");
    public static DiscordColor Info => Blue;
    
    public static DiscordColor Urban => FromHex("#1b2936");
    public static DiscordColor Reddit => FromHex("#ff4500");
    public static DiscordColor DiscordLegacy => FromHex("#7289da");
    public static DiscordColor Discord => FromHex("#5865f2");
    public static DiscordColor Twitter => FromHex("#1da1f2");
    public static DiscordColor Github => FromHex("#333333");
    public static DiscordColor Youtube => FromHex("#ff0000");
    
    public static DiscordColor[] Rainbow => new[] {
        Red,
        Orange,
        Yellow,
        Green,
        Mint,
        Cyan,
        Blue,
        Purple,
        Pink
    };
    
    public static DiscordColor Random => Rainbow[new Random().Next(0, Rainbow.Length)];

    public static DiscordColor FromHex(string hex) {
        hex = hex.Replace("#", "");
        var r = byte.Parse(hex[..2], NumberStyles.HexNumber);
        var g = byte.Parse(hex.Substring(2, 2), NumberStyles.HexNumber);
        var b = byte.Parse(hex.Substring(4, 2), NumberStyles.HexNumber);
        return new DiscordColor(r, g, b);
    }
}