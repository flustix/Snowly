package flustix.fluxifyed.database.api.utils.ratelimit;

public class RateLimitData {
    public int secondsLeft;
    public int requestsLeft;

    public RateLimitData(long time, int requestsLeft) {
        this.secondsLeft = 60 - (int)((System.currentTimeMillis() - time) / 1000);
        this.requestsLeft = requestsLeft;
    }
}
