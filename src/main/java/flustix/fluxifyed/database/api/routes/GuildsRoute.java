package flustix.fluxifyed.database.api.routes;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.APIGuild;
import flustix.fluxifyed.database.api.types.Route;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.List;

public class GuildsRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        Headers headers = exchange.getRequestHeaders();

        List<String> tokenHeader = headers.get("Authorization");
        String token = "";

        if (tokenHeader == null)
            return null;

        token = tokenHeader.get(0);

        String userid = AuthUtils.getUserId(token);
        JsonObject json = new JsonObject();
        JsonArray guilds = new JsonArray();

        for (Guild guild : AuthUtils.getServers(userid)) {
            guilds.add(new GsonBuilder()
                    .create()
                    .toJson(new APIGuild(guild))
            );
        }

        json.add("guilds", guilds);
        return json;
    }
}
