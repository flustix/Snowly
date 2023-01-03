package flustix.fluxifyed.utils;

public class TimeUtils {
    public static String format(long time) {
        long days = time / 86400000;
        long hours = (time % 86400000) / 3600000;
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;

        return (days > 0 ? days + "d " : "") + (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + seconds + "s";
    }
}
