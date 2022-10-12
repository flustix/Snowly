package flustix.fluxifyed.database.api.v1.routes.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.APIServer;
import flustix.fluxifyed.database.api.v1.authentification.AuthUtils;
import flustix.fluxifyed.database.api.v1.authentification.TokenGen;
import flustix.fluxifyed.database.api.v1.components.APIUser;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import net.dv8tion.jda.api.entities.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@APIRoute(path = "/login/discord")
public class DiscordLoginRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        String token = AuthUtils.getToken(exchange.getRequestHeaders());
        if (token.isEmpty())
            return new APIResponse(401, "No token given.", null);

        String userid = "";

        try {
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
            HttpRequest request = HttpRequest.newBuilder(new URI("https://discord.com/oauth2/@me"))
                    .header("Accept", "application/json, text/plain, /")
                    .header("Authorization", "Bearer " + token)
                    .header("User-Agent", "Fluxifyed/" + Main.getVersion() + " (by Flustix#5433)")
                    .build();

            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());

            APIServer.LOGGER.info("Discord Login Response: " + res.statusCode() + " " + res.body());

            if (res.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();
                JsonObject userJson = json.getAsJsonObject("user");

                userid = userJson.get("id").getAsString();

                User user = Main.getBot().getUserById(userid);

                if (user == null)
                    user = Main.getBot().retrieveUserById(userid).complete();

                String newToken = TokenGen.generateToken(userid);

                return new APIResponse(200, "OK", new APIUserToken(newToken, new APIUser(user)));
            } else {
                return new APIResponse(401, "Invalid Token.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse(404, "User not found.", null);
        }
    }

    private static class APIUserToken {
        public String token;
        public APIUser user;

        public APIUserToken(String token, APIUser user) {
            this.token = token;
            this.user = user;
        }
    }
}
