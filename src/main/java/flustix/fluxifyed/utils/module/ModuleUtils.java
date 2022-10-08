package flustix.fluxifyed.utils.module;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.components.SlashCommandList;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ModuleUtils {
    public static void loadCommands(Module module) {
        List<SlashCommand> commands = new ArrayList<>();

        Reflections reflections = new Reflections("flustix.fluxifyed.modules." + module.id + ".commands");
        reflections.getSubTypesOf(SlashCommand.class).forEach(c -> {
            try {
                commands.add(c.getConstructor().newInstance());
            } catch (Exception e) {
                Main.LOGGER.error("Error while loading command " + c.getName() + " from module " + module.id);
            }
        });

        SlashCommandList.addModuleCommands(module.id, commands);
    }

    public static List<ListenerAdapter> loadListeners(Module module) {
        List<ListenerAdapter> listeners = new ArrayList<>();

        Reflections reflections = new Reflections("flustix.fluxifyed.modules." + module.id + ".listeners");
        reflections.getSubTypesOf(ListenerAdapter.class).forEach(c -> {
            try {
                listeners.add(c.getConstructor().newInstance());
            } catch (Exception e) {
                Main.LOGGER.error("Error while loading listener " + c.getName() + " from module " + module.id);
            }
        });

        return listeners;
    }
}
