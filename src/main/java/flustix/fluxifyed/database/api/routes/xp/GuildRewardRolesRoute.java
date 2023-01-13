package flustix.fluxifyed.database.api.routes.xp;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.api.components.xp.RewardRoles;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/rewardroles/:guild")
public class GuildRewardRolesRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        Guild guild = Main.getBot().getGuildById(params.get("guild"));

        if (guild == null) return new APIResponse(404, "Guild not found", null);

        return new APIResponse(200, "OK", new RewardRoles(guild));
    }
}
