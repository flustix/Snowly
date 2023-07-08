using Fluxifyed.CC.Commands;
using Fluxifyed.Logging;

namespace Fluxifyed.CC; 

public static class ConsoleCommands {
    public static readonly List<IConsoleCommand> Commands = new();
    private static bool _run = true;

    public static void Init() {
        Register(new HelpConsoleCommand());
        Register(new GuildsConsoleCommand());
        Register(new StopConsoleCommand());
        Register(new SayConsoleCommand());
        Register(new ReplyConsoleCommand());
        Register(new ModulesConsoleCommand());
        
        Commands.Sort((a, b) => string.Compare(a.Name, b.Name, StringComparison.Ordinal));
        
        var thread = new Thread(Listen) { IsBackground = true };
        thread.Start();
    }
    
    private static void Register(IConsoleCommand command) {
        // Logger.Log($"Registered console command '{command.Name}'");
        Commands.Add(command);
    }
    
    public static void Stop() {
        _run = false;
    }

    private static void Listen() {
        while (_run) {
            var input = Console.ReadLine();

            if (input == null) continue;
            var split = input.Split(" ");
            var args = split.Skip(1).ToArray();
                
            var commandName = split[0];
            var command = Commands.FirstOrDefault(c => c.Name == commandName);

            try {
                command?.Execute(args);
                    
                if (command == null) {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.Write($"Command '{commandName}' not found!\n");
                    Logger.EmptyLine();
                }
            }
            catch (Exception e) {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.Write($"Error: {e.Message}\n");
                Logger.EmptyLine();
            }
        }
    }
}