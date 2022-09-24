package flustix.fluxifyed.database.api.routes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.command.SlashCommandList;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.Map;

public class CommandsRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        JsonObject json = new JsonObject();

        JsonArray commands = new JsonArray();

        for (Map.Entry<String, SlashCommand> entry : SlashCommandList.getCommands().entrySet()) {
            SlashCommand slashCommand = entry.getValue();
            JsonObject commandJson = new JsonObject();
            commandJson.addProperty("name", slashCommand.getName());
            commandJson.addProperty("description", slashCommand.getDescription());

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

            commandJson.addProperty("permissionLevel", slashCommand.getPermissionLevel().toString());
            commands.add(commandJson);
        }

        json.add("commands", commands);
        return json;
    }
}
