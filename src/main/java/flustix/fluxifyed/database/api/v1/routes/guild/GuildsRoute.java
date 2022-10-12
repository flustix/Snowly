package flustix.fluxifyed.database.api.v1.routes.guild;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.components.APIGuild;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

@APIRoute(path = "/guilds")
public class GuildsRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());

        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        JsonArray guilds = new JsonArray();

        for (Guild guild : AuthUtils.getServers(userid)) {
            guilds.add(JSONUtils.toJson(new APIGuild(guild)));
        }

        return new APIResponse(200, "OK", guilds);
    }
}
