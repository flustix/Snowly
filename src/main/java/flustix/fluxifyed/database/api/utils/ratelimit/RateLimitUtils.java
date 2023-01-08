package flustix.fluxifyed.database.api.utils.ratelimit;

import java.util.HashMap;

public class RateLimitUtils {
    private static final HashMap<String, RateLimitIP> ips = new HashMap<>();

    public static void addRateLimit(String ip, String path) {
        RateLimitIP rateLimitIP = ips.getOrDefault(ip, new RateLimitIP());
        rateLimitIP.addRateLimit(path);
        ips.put(ip, rateLimitIP);
    }

    public static RateLimitData isRateLimited(String ip, String path, int rateLimit) {
        RateLimitIP rateLimitIP = ips.getOrDefault(ip, new RateLimitIP());
        return rateLimitIP.isRateLimited(path, rateLimit);
    }
}
