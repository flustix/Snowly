package flustix.fluxifyed.database.api.routes.modules.xp.dash;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/levelroles/:guild")
public class LevelRolesRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        if (!AuthUtils.hasAccess(userid, params.get("guild")))
            return new APIResponse(403, "You don't have access to this guild.", null);

        XPGuild guild = XP.getGuild(params.get("guild"));

        JsonArray json = new JsonArray();

        for (XPRole role : guild.getLevelRoles()) {
            JsonObject roleJson = new JsonObject();
            roleJson.addProperty("id", role.getID());
            roleJson.addProperty("level", role.getValue());
            json.add(roleJson);
        }

        return new APIResponse(200, "OK", json);
    }
}
