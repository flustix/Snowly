package flustix.fluxifyed.database.api.v1.routes.xp;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPUser;

import java.util.HashMap;

public class XPUserRoute implements Route {
    @Override
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        JsonObject json = new JsonObject();

        XPGuild guild = XP.getGuild(params.get("guild"));
        if (guild == null)
            return json; // return nothing if it doesnt exist :shrug:

        XPUser user = guild.getUser(params.get("user"));
        if (user == null)
            return json; // return nothing if it doesnt exist :shrug:

        json.addProperty("guild", guild.getID());
        json.addProperty("user", user.getID());
        json.addProperty("xp", user.getXP());

        return json;
    }
}
