package flustix.fluxifyed.database.api.routes.xp;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.components.xp.RewardRoles;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/rewardroles/:guild")
public class GuildRewardRolesRoute implements Route {
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

        Guild guild = Main.getBot().getGuildById(params.get("guild"));

        if (guild == null) return new APIResponse(404, "Guild not found", null);

        return new APIResponse(200, "OK", new RewardRoles(guild));
    }
}
