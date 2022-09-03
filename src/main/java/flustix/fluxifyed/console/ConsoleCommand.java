package flustix.fluxifyed.console;

public class ConsoleCommand {
    public String name;
    public String description;

    public ConsoleCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void execute(String[] args) {
        System.out.println("This command is not implemented yet.");
    }
}
