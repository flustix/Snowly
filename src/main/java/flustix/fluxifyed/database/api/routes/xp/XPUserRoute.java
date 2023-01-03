package flustix.fluxifyed.database.api.routes.xp;


import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.components.xp.UserStats;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/stats/:guild/:user")
public class XPUserRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String guildid = params.get("guild");
        String userid = params.get("user");

        XPGuild guild = XP.getGuild(guildid);
        if (guild == null) return new APIResponse(404, "Guild not found", null);

        if (!guild.hasUser(userid)) return new APIResponse(404, "User not found", null);

        XPUser user = guild.getUser(userid);

        return new APIResponse(200, "OK", new UserStats(user, guild));
    }
}
