package flustix.fluxifyed.modules.fun.utils.higherlower;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.modules.fun.utils.higherlower.components.HigherLowerGame;
import flustix.fluxifyed.modules.fun.utils.higherlower.components.HigherLowerOption;
import flustix.fluxifyed.modules.fun.utils.higherlower.components.HigherLowerRound;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HigherLowerUtils {
    private static final HashMap<String, HigherLowerGame> games = new HashMap<>(); // <user id, game>
    private static List<HigherLowerOption> options;

    public static void startGame(InteractionHook hook) {
        if (options == null) fetchOptions(hook);

        List<HigherLowerRound> rounds = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HigherLowerOption option1 = options.get((int) (Math.random() * options.size()));
            HigherLowerOption option2 = options.get((int) (Math.random() * options.size()));
            int correctOption = option1.getValue() > option2.getValue() ? 1 : 2;

            rounds.add(new HigherLowerRound(option1, option2, correctOption));
        }

        HigherLowerGame game = new HigherLowerGame(hook, rounds);
        game.start();

        games.put(hook.getInteraction().getUser().getId(), game);
    }

    private static void fetchOptions(InteractionHook hook) {
        options = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
            HttpRequest req = HttpRequest.newBuilder(URI.create("https://www.higherlowergame.com/questions/get/general"))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            JsonArray json = JsonParser.parseString(res.body()).getAsJsonArray();

            for (int i = 0; i < json.size(); i++) {
                JsonObject obj = json.get(i).getAsJsonObject();

                String name = obj.get("keyword").getAsString();
                int value = obj.get("searchVolume").getAsInt();

                options.add(new HigherLowerOption(name, value));
            }
        } catch (Exception e) {
            hook.editOriginal("An error occurred while starting.").queue();
            e.printStackTrace();
        }
    }

    public static HigherLowerGame getGame(String userId) {
        return games.get(userId);
    }

    public static void endGame(String userId) {
        games.remove(userId);
    }
}
