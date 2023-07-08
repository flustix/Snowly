using Microsoft.Extensions.Logging;

namespace Fluxifyed.Logging; 

public class Logger : ILogger {
    private string Name { get; init; }
    
    public Logger(string name) {
        Name = name;
    }

    public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter) {
        if (logLevel == LogLevel.Trace || (logLevel == LogLevel.Debug && !Fluxifyed.IsDebug)) return;

        // ReSharper disable once SwitchExpressionHandlesSomeKnownEnumValuesWithExceptionInDefault
        var severity = logLevel switch {
            LogLevel.Critical => "Critical",
            LogLevel.Debug => "Debug",
            LogLevel.Information => "Info",
            LogLevel.Warning => "Warn",
            LogLevel.Error => "Error",
            LogLevel.None => "???",
            _ => throw new ArgumentOutOfRangeException(nameof(logLevel), logLevel, null)
        };
        
        severity = severity.PadRight(8)[..8];
        
        var source = Name.Split('.').Last().PadRight(12)[..12];
        
        var msg = formatter(state, exception);

        ClearCurrentConsoleLine();
        Console.ForegroundColor = ConsoleColor.Gray;
        Console.Write($"[{DateTime.Now:HH:mm:ss}] ");
        Console.ForegroundColor = logLevel switch {
            LogLevel.Critical => ConsoleColor.DarkRed,
            LogLevel.Error => ConsoleColor.Red,
            LogLevel.Warning => ConsoleColor.Yellow,
            LogLevel.Information => ConsoleColor.Cyan,
            LogLevel.Debug => ConsoleColor.Magenta,
            _ => ConsoleColor.White
        };
        Console.Write($"{severity} ");
        Console.ForegroundColor = ConsoleColor.Green;
        Console.Write($"{source} ");
        Console.ForegroundColor = ConsoleColor.White;
        Console.Write($"{msg}\n");
        
        if (exception != null) {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(exception.Message);
            exception.StackTrace?.Split('\n').ToList().ForEach(x => Console.WriteLine(x.Trim()));
        }
        
        // EmptyLine();
    }
    
    public static void EmptyLine() {
        Console.ForegroundColor = ConsoleColor.White;
        Console.Write("> ");
    }

    private static void ClearCurrentConsoleLine()
    {
        Console.SetCursorPosition(0, Console.CursorTop);
        Console.Write(new string(' ', Console.WindowWidth));
        Console.SetCursorPosition(0, Console.CursorTop);
    }

    public bool IsEnabled(LogLevel logLevel) {
        throw new NotImplementedException();
    }

    public IDisposable BeginScope<TState>(TState state) {
        throw new NotImplementedException();
    }
}