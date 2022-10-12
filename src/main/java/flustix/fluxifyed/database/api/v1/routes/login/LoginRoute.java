package flustix.fluxifyed.database.api.v1.routes.login;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.components.APIUser;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

@APIRoute(path = "/login")
public class LoginRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            return new APIResponse(401, "Invalid Token.", null);

        try {
            User user = Main.getBot().getUserById(userid);

            if (user == null)
                user = Main.getBot().retrieveUserById(userid).complete();

            return new APIResponse(200, "OK", new APIUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse(404, "User not found.", null);
        }
    }
}
