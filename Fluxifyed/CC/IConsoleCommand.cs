namespace Fluxifyed.CC; 

public interface IConsoleCommand {
    public string Name { get; }
    public string Usage { get; }
    public string Description { get; }
    public void Execute(string[] args);
}