package flustix.fluxifyed.database.api.v1.routes;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.components.APIUser;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;

public class LoginRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        Headers headers = exchange.getRequestHeaders();
        List<String> tokenHeader = headers.get("Authorization");

        String token = "";

        if (tokenHeader == null)
            throw new Exception("Error: No token provided");

        token = tokenHeader.get(0);
        String userid = AuthUtils.getUserId(token);

        if (userid.isEmpty())
            throw new Exception("Invalid token");

        try {
            User user = Main.getBot().getUserById(userid);

            if (user == null)
                user = Main.getBot().retrieveUserById(userid).complete();

            return JSONUtils.toJson(new APIUser(user)).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("User not found");
        }
    }
}
