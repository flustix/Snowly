using Microsoft.Extensions.Logging;

namespace Snowly.Logging;

public class LoggerFactory : ILoggerFactory
{
    public void Dispose()
    {
        GC.SuppressFinalize(this);
    }

    public ILogger CreateLogger(string categoryName) => new Logger();

    public void AddProvider(ILoggerProvider provider)
    {
    }
}
