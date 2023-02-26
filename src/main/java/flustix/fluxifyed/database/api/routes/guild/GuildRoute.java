package flustix.fluxifyed.database.api.routes.guild;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.guild.APIGuildDashboard;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.IRoute;

import java.util.HashMap;

@APIRoute(path = "/guild/:id")
public class GuildRoute implements IRoute {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        String guildid = params.get("id");

        APIGuildDashboard guild = AuthUtils.getGuild(userid, guildid);

        if (guild == null)
            return new APIResponse(403, "You are not an admin in this guild!", null);

        return new APIResponse(200, "OK", guild);
    }
}
