package flustix.fluxifyed.database.api.routes;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.IRoute;
import flustix.fluxifyed.utils.JSONUtils;

import java.util.HashMap;

@APIRoute(path = "/modules")
public class ModulesRoute implements IRoute {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        JsonArray modules = new JsonArray();

        for (Module module : Main.getModules()) {
            modules.add(JSONUtils.toJson(module));
        }

        return new APIResponse(200, "OK", modules);
    }
}
