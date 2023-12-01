using Microsoft.Extensions.Logging;

namespace Snowly.Logging;

public class LoggerFactory : ILoggerFactory {
    public void Dispose() {
        GC.SuppressFinalize(this);
    }

    public ILogger CreateLogger(string categoryName) {
        return new Logger(categoryName);
    }

    public void AddProvider(ILoggerProvider provider) { }
}
