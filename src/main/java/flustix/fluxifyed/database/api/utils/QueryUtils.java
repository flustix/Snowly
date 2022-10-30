package flustix.fluxifyed.database.api.utils;

import java.util.HashMap;

public class QueryUtils {
    public static HashMap<String, String> getQuery(String query) {
        HashMap<String, String> map = new HashMap<>();

        if (query == null) return map;

        for (String q : query.split("&")) {
            try {
                String[] split = q.split("=");
                map.put(split[0], split[1]);
            } catch (Exception ignored) {}
        }

        return map;
    }
}
