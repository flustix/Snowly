package flustix.fluxifyed.database.api.routes.timers;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.authentification.AuthUtils;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.modules.timers.TimersModule;
import flustix.fluxifyed.modules.timers.components.TimedMessage;

import java.util.HashMap;

@APIRoute(path = "/modules/timers/:id/test")
public class TestTimerRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        TimedMessage message = TimersModule.getTimer(Integer.parseInt(params.get("id")));
        if (message == null) return new APIResponse(404, "Timer not found.", null);

        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty()) return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty()) return new APIResponse(401, "Invalid Token.", null);

        if (!AuthUtils.hasAccess(userid, message.getGuildId()))
            return new APIResponse(403, "You don't have access to this guild.", null);

        message.send(true);

        return new APIResponse(200, "OK", null);
    }
}
