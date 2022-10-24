package flustix.fluxifyed.modules.utility.utils.urban;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.modules.utility.utils.urban.components.UrbanDefinition;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class UrbanUtils {
    private static final String URBAN_API_URL = "https://api.urbandictionary.com/v0/define?term=";
    private static final String URBAN_URL = "https://www.urbandictionary.com/define.php?term=";

    private static final HashMap<String, UrbanDefinition> definitions = new HashMap<>();

    public static UrbanDefinition getDefinition(String word) {
        if (definitions.containsKey(word)) {
            return definitions.get(word);
        } else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URBAN_API_URL + URLEncoder.encode(word, StandardCharsets.UTF_8)))
                    .build();

            String response;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            if (response == null) return null;

            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            if (json.get("list").getAsJsonArray().size() == 0) return null;

            JsonObject definitionJson = json.get("list").getAsJsonArray().get(0).getAsJsonObject();

            UrbanDefinition definition = new UrbanDefinition(
                    definitionJson.get("word").getAsString(),
                    definitionJson.get("definition").getAsString(),
                    definitionJson.get("example").getAsString(),
                    definitionJson.get("author").getAsString(),
                    definitionJson.get("permalink").getAsString(),
                    definitionJson.get("thumbs_up").getAsInt(),
                    definitionJson.get("thumbs_down").getAsInt()
            );
            definitions.put(word, definition);
            return definition;
        }
    }

    public static String getUrbanUrl() {
        return URBAN_URL;
    }
}
