package flustix.fluxifyed.database.api.v1.routes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;

import java.util.HashMap;

@APIRoute(path = "/modules")
public class ModulesRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        JsonArray modules = new JsonArray();

        for (Module module : Main.getModules()) {
            modules.add(JSONUtils.toJson(module));
        }

        return new APIResponse(200, "OK", modules);
    }
}
