namespace Snowly.Utils;

public static class FormatUtils
{
    private const long second = 1000;
    private const long minute = 60 * second;
    private const long hour = 60 * minute;
    private const long day = 24 * hour;
    private const long month = 30 * day;
    private const long year = 365 * day;

    public static string FormatTime(long time, bool ms = true)
    {
        var years = time / year;
        time %= year;
        var months = time / month;
        time %= month;
        var days = time / day;
        time %= day;
        var hours = time / hour;
        time %= hour;
        var minutes = time / minute;
        time %= minute;
        var seconds = time / second;
        time %= second;
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

    public static string FormatName(string name)
    {
        if (string.IsNullOrEmpty(name)) return "";

        return name.EndsWith("s") ? $"{name}'" : $"{name}'s";
    }
}
