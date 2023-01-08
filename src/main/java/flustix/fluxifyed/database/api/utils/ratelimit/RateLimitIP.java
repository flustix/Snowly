package flustix.fluxifyed.database.api.utils.ratelimit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateLimitIP {
    private final HashMap<String, ArrayList<RateLimit>> rateLimits = new HashMap<>();

    public RateLimitIP() {
    }

    public void addRateLimit(String path) {
        ArrayList<RateLimit> rateLimitList = rateLimits.getOrDefault(path, new ArrayList<>());
        rateLimitList.add(new RateLimit());
        rateLimits.put(path, rateLimitList);
    }

    public RateLimitData isRateLimited(String path, int rateLimit) {
        updateRateLimits();

        List<RateLimit> rateLimitList = rateLimits.getOrDefault(path, new ArrayList<>());
        if (rateLimitList.size() == 0) {
            return new RateLimitData(System.currentTimeMillis(), rateLimit);
        } else if (rateLimitList.size() < rateLimit) {
            return new RateLimitData(rateLimitList.get(0).getTime(), rateLimit - rateLimitList.size());
        } else {
            return new RateLimitData(rateLimitList.get(0).getTime(), 0);
        }
    }

    public void updateRateLimits() {
        rateLimits.forEach((path, rateLimitList) -> rateLimitList.removeIf(RateLimit::isExpired));
    }

    private static class RateLimit {
        private final long time;

        public RateLimit() {
            this.time = System.currentTimeMillis();
        }

        public long getTime() {
            return time;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - time > 60000;
        }
    }
}
