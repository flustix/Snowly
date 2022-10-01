package flustix.fluxifyed.database.api.v1.routes.guild;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;

import java.util.HashMap;

public class GuildRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty()) return null;

        return JSONUtils.toJson(AuthUtils.getGuild(AuthUtils.getUserId(token), params.get("id"))).getAsJsonObject();
    }
}
