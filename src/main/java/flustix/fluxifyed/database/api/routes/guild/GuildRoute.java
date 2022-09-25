package flustix.fluxifyed.database.api.routes.guild;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.APIGuild;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;

import java.util.HashMap;

public class GuildRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty()) return null;

        return JSONUtils.toJson(AuthUtils.getGuild(AuthUtils.getUserId(token), params.get("id"))).getAsJsonObject();
    }
}
