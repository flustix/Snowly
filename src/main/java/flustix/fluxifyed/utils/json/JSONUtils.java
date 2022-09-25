package flustix.fluxifyed.utils.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class JSONUtils {
    public static JsonElement toJson(Object object) {
        return new GsonBuilder().create().toJsonTree(object);
    }
}
