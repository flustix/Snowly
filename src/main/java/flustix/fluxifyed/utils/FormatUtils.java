package flustix.fluxifyed.utils;

import java.util.concurrent.TimeUnit;

public class FormatUtils {
    private static final long yearMillis = TimeUnit.DAYS.toMillis(365);
    private static final long monthMillis = TimeUnit.DAYS.toMillis(30);
    private static final long dayMillis = TimeUnit.DAYS.toMillis(1);
    private static final long hourMillis = TimeUnit.HOURS.toMillis(1);
    private static final long minuteMillis = TimeUnit.MINUTES.toMillis(1);
    private static final long secondMillis = TimeUnit.SECONDS.toMillis(1);

    public static String timeToString(long time) {
        long years = time / yearMillis;
        long months = (time % yearMillis) / monthMillis;
        long days = (time % monthMillis) / dayMillis;
        long hours = (time % dayMillis) / hourMillis;
        long minutes = (time % hourMillis) / minuteMillis;
        long seconds = (time % minuteMillis) / secondMillis;

        return (years > 0 ? years + "y " : "") + (months > 0 ? months + "mo " : "") + (days > 0 ? days + "d " : "") + (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s " : "");
    }

    public static String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            return String.format("%.1fK", number / 1000D);
        } else if (number < 1000000000) {
            return String.format("%.1fM", number / 1000000D);
        } else {
            return String.format("%.1fB", number / 1000000000D);
        }
    }
}
