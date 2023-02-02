package flustix.fluxifyed.database.api.routes.modules.xp;


import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.xp.UserStats;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.settings.Settings;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/stats/:guild/:user")
public class XPUserStatsRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        if (Settings.hasSettings(params.get("guild"))) {
            if (Settings.getGuildSettings(params.get("guild")).getBoolean("xp.requireAuth", false)) {
                String token = AuthUtils.getToken(exchange.getRequestHeaders());
                if (token.isEmpty())
                    return new APIResponse(401, "No token given.", null);

                String userid = AuthUtils.getUserId(token);

                if (userid.isEmpty())
                    return new APIResponse(401, "Invalid Token.", null);

                if (!AuthUtils.isOnGuild(userid, params.get("guild")))
                    return new APIResponse(403, "You are not on this guild.", null);
            }
        }

        String guildid = params.get("guild");
        String userid = params.get("user");

        XPGuild guild = XP.getGuild(guildid);
        if (guild == null) return new APIResponse(404, "Guild not found", null);

        if (!guild.hasUser(userid)) return new APIResponse(404, "User not found", null);

        XPUser user = guild.getUser(userid);

        return new APIResponse(200, "OK", new UserStats(user, guild));
    }
}
