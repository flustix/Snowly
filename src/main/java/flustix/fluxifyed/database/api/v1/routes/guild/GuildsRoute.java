package flustix.fluxifyed.database.api.v1.routes.guild;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.components.APIGuild;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildsRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());

        if (token.isEmpty()) return null;

        String userid = AuthUtils.getUserId(token);
        JsonObject json = new JsonObject();
        JsonArray guilds = new JsonArray();

        for (Guild guild : AuthUtils.getServers(userid)) {
            guilds.add(JSONUtils.toJson(new APIGuild(guild)));
        }

        json.add("guilds", guilds);
        return json;
    }
}
