namespace Fluxifyed.Utils; 

public static class FormatUtils {
    private const long Second = 1000;
    private const long Minute = 60 * Second;
    private const long Hour = 60 * Minute;
    private const long Day = 24 * Hour;
    private const long Month = 30 * Day;
    private const long Year = 365 * Day;
    
    public static string FormatTime(long time, bool ms = true) {
        var years = time / Year;
        time %= Year;
        var months = time / Month;
        time %= Month;
        var days = time / Day;
        time %= Day;
        var hours = time / Hour;
        time %= Hour;
        var minutes = time / Minute;
        time %= Minute;
        var seconds = time / Second;
        time %= Second;
        var milliseconds = time;
        
        var formatted = "";
        if (years > 0) formatted += $"{years}y ";
        if (months > 0) formatted += $"{months}mo ";
        if (days > 0) formatted += $"{days}d ";
        if (hours > 0) formatted += $"{hours}h ";
        if (minutes > 0) formatted += $"{minutes}m ";
        if (seconds > 0) formatted += $"{seconds}s ";
        if (milliseconds > 0 && ms) formatted += $"{milliseconds}ms ";
        return formatted.Trim();
    }

    public static string FormatName(string name) {
        if (string.IsNullOrEmpty(name)) return "";
        
        if (name.EndsWith("s")) return $"{name}'";
        return $"{name}'s";
    }
}