package flustix.fluxifyed.database.api.v1.routes.guild;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.components.APIGuild;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.util.HashMap;

@APIRoute(path = "/guild/:id")
public class GuildRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        String guildid = params.get("id");

        APIGuild guild = AuthUtils.getGuild(userid, guildid);

        if (guild == null)
            return new APIResponse(403, "You are not an admin in this guild!", null);

        return new APIResponse(200, "OK", guild);
    }
}
