package flustix.fluxifyed.database.api.routes.xp.dash;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/settings/:guild")
public class XPSettingsRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        if (!AuthUtils.hasAccess(userid, params.get("guild")))
            return new APIResponse(403, "You don't have access to this guild.", null);

        GuildSettings settings = Settings.getGuildSettings(params.get("guild"));

        return new APIResponse(200, "OK", settings.getAll("xp"));
    }
}
