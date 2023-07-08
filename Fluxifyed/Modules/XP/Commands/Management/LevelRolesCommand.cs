using DSharpPlus.Entities;
using Fluxifyed.Commands;
using Fluxifyed.Utils;

namespace Fluxifyed.Modules.XP.Commands.Management; 

public class LevelRolesCommand : ISlashCommand {
    public string Name => "level-roles";
    public string Description => "Manage level roles";
    
    public void Handle(DiscordInteraction interaction) {
        interaction.Reply("This command is not implemented yet.", true);
    }
}