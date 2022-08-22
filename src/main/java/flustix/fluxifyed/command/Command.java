package flustix.fluxifyed.command;

import flustix.fluxifyed.utils.permissions.PermissionLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Command {
    private final String name;
    private final String description;
    private final PermissionLevel permissionLevel;

    public Command(String name, String desc, PermissionLevel level) {
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty!");

        this.name = name;
        this.description = desc;
        this.permissionLevel = level;
    }

    public void execute(MessageReceivedEvent event, String[] args) throws Exception {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }
}
