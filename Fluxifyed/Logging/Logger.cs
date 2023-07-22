using Microsoft.Extensions.Logging;

namespace Fluxifyed.Logging;

public class Logger : ILogger {
    private string name { get; }

    public Logger(string name) {
        this.name = name;
    }

    public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter) {
        if (logLevel == LogLevel.Trace || (logLevel == LogLevel.Debug && !Fluxifyed.IsDebug)) return;

        // ReSharper disable once SwitchExpressionHandlesSomeKnownEnumValuesWithExceptionInDefault
        var severity = getSeverity(logLevel).PadRight(8)[..8];
        var msg = formatter(state, exception);

        Console.ForegroundColor = ConsoleColor.Gray;
        Console.Write($"[{DateTime.Now:HH:mm:ss}] ");
        Console.ForegroundColor = getColor(logLevel);
        Console.Write($"{severity} ");
        Console.ForegroundColor = ConsoleColor.White;
        Console.Write($"{msg}\n");

        if (exception != null) {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(exception.Message);
            exception.StackTrace?.Split('\n').ToList().ForEach(x => Console.WriteLine(x.Trim()));
        }

        var log = $"{DateTime.Now:yyyy-MM-dd} {DateTime.Now:HH:mm:ss} [{severity.ToLower().Trim()}]: {msg}";
        File.AppendAllText("fluxifyed.log", log + "\n");
        exception?.StackTrace?.Split('\n').ToList().ForEach(x => File.AppendAllText("fluxifyed.log", x.Trim() + "\n"));
    }

    private ConsoleColor getColor(LogLevel logLevel) => logLevel switch {
        LogLevel.Critical => ConsoleColor.DarkRed,
        LogLevel.Error => ConsoleColor.Red,
        LogLevel.Warning => ConsoleColor.Yellow,
        LogLevel.Information => ConsoleColor.Cyan,
        LogLevel.Debug => ConsoleColor.Magenta,
        _ => ConsoleColor.White
    };

    private string getSeverity(LogLevel logLevel) => logLevel switch {
        LogLevel.Critical => "Critical",
        LogLevel.Error => "Error",
        LogLevel.Warning => "Warning",
        LogLevel.Information => "Info",
        LogLevel.Debug => "Debug",
        _ => "???"
    };

    public bool IsEnabled(LogLevel logLevel) => throw new NotImplementedException();
    public IDisposable BeginScope<TState>(TState state) => throw new NotImplementedException();
}
