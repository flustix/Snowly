package flustix.fluxifyed.modules.xp.utils;

public class XPUtils {
    public static long calculateLevel(long xp) {
        return calculateLevel(xp, "default");
    }

    /**
     * Calculates level from xp with different formulas from other bots (for easier adapting)
     */
    public static long calculateLevel(long xp, String formula) {
        switch (formula) {
            case "amari" -> {
                return (long) Math.sqrt((xp - 35) / 20f) + 1;
            }
            default -> {
                return (long) Math.floor(Math.sqrt(xp / 100f));
            }
        }
    }

    public static long calculateXP(long level) {
        return calculateXP(level, "default");
    }

    /**
     * Calculates xp from level with different formulas from other bots (for easier adapting)
     */
    public static long calculateXP(long level, String formula) {
        switch (formula) {
            case "amari" -> {
                return (int)(20 * Math.pow(level - 1, 2) + 35);
            }
            default -> {
                return 100 * (level * level);
            }
        }
    }
}
