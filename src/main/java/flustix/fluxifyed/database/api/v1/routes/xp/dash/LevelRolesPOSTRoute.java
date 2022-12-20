package flustix.fluxifyed.database.api.v1.routes.xp.dash;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@APIRoute(path = "/modules/xp/levelroles/:guild", method = "POST")
public class LevelRolesPOSTRoute implements Route {
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

        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            JsonArray json = JsonParser.parseString(body).getAsJsonArray();

            List<XPRole> roles = new ArrayList<>();

            for (JsonElement role : json) {
                JsonObject roleJson = role.getAsJsonObject();
                XPRole xpRole = new XPRole(roleJson.get("id").getAsString(), roleJson.get("level").getAsInt());
                roles.add(xpRole);
            }

            guild.rebuildLevelRoles(roles);
        } catch (Exception e) {
            return new APIResponse(400, "Invalid body.", null);
        }

        return new APIResponse(200, "OK", null);
    }
}
