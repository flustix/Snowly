using DSharpPlus.Entities;
using Snowly.Commands;
using Snowly.Utils;

namespace Snowly.Modules.XP.Commands.Management.Multipliers;

public class RoleMultipliersListCommand : ISlashCommand {
    public string Name => "list-roles";
    public string Description => "List all role multipliers for the server.";

    public void Handle(DiscordInteraction interaction) {
        interaction.Reply("This command is not implemented yet.", true);
    }
}
