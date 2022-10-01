package flustix.fluxifyed.database.api.v1.routes;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.types.Route;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;

public class LoginRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        JsonObject json = new JsonObject();

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

            json.addProperty("userid", userid);
            json.addProperty("username", user.getName());
            json.addProperty("discriminator", user.getDiscriminator());
            json.addProperty("avatar", user.getEffectiveAvatarUrl() + "?size=1024");

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("User not found");
        }
    }
}
