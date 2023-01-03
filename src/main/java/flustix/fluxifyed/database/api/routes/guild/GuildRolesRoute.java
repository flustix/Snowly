package flustix.fluxifyed.database.api.routes.guild;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.guild.GuildRole;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.utils.JSONUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;

@APIRoute(path = "/guild/:guild/roles")
public class GuildRolesRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        if (!AuthUtils.hasAccess(userid, params.get("guild")))
            return new APIResponse(403, "You don't have access to this guild.", null);

        Guild guild = Main.getBot().getGuildById(params.get("guild"));
        if (guild == null) return new APIResponse(404, "Guild not found.", null);

        JsonArray json = new JsonArray();

        for (Role role : guild.getRoles()) {
            if (!role.getName().equals("@everyone") && !role.isManaged()) {
                json.add(JSONUtils.toJson(new GuildRole(role)));
            }
        }

        return new APIResponse(200, "OK", json);
    }
}
