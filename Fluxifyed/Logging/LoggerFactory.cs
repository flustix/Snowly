using Microsoft.Extensions.Logging;

namespace Fluxifyed.Logging; 

public class LoggerFactory : ILoggerFactory {
    public void Dispose() {
        
    }

    public ILogger CreateLogger(string categoryName) {
        return new Logger(categoryName);
    }

    public void AddProvider(ILoggerProvider provider) {
        
    }
}