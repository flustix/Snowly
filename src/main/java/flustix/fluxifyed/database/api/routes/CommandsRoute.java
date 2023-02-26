package flustix.fluxifyed.database.api.routes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.components.SlashCommandList;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.IRoute;
import flustix.fluxifyed.localization.Localization;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@APIRoute(path = "/commands")
public class CommandsRoute implements IRoute {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        JsonArray commands = new JsonArray();

        for (Map.Entry<String, TreeMap<String, SlashCommand>> module : SlashCommandList.getCommands().entrySet()) {
            JsonObject moduleObject = new JsonObject();
            JsonArray moduleCommands = new JsonArray();

            for (Map.Entry<String, SlashCommand> command : module.getValue().entrySet()) {
                SlashCommand slashCommand = command.getValue();
                JsonObject commandJson = new JsonObject();
                commandJson.addProperty("name", slashCommand.getName());
                commandJson.addProperty("description", Localization.get("command.description." + module.getKey() + "." + slashCommand.getName()));

                JsonArray options = new JsonArray();
                for (OptionData option : slashCommand.getOptions()) {
                    JsonObject optionJson = new JsonObject();
                    optionJson.addProperty("name", option.getName());
                    optionJson.addProperty("description", option.getDescription());
                    optionJson.addProperty("required", option.isRequired());
                    optionJson.addProperty("type", option.getType().toString());
                    options.add(optionJson);
                }
                commandJson.add("options", options);

                JsonArray permissions = new JsonArray();
                for (Permission permission : slashCommand.getPermissions()) {
                    permissions.add(permission.toString());
                }
                commandJson.add("permissions", permissions);
                moduleCommands.add(commandJson);
            }

            moduleObject.addProperty("name", module.getKey());
            moduleObject.add("commands", moduleCommands);

            commands.add(moduleObject);
        }

        return new APIResponse(200, "OK", commands);
    }
}
