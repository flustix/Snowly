package flustix.fluxifyed.utils.xp;

public class XPUtils {
    public static int calculateLevel(int xp) {
        return calculateLevel(xp, "default");
    }

    /**
     * Calculates level from xp with different formulas from other bots (for easier adapting)
     */
    public static int calculateLevel(int xp, String formula) {
        switch (formula) {
            case "amari" -> {
                return (int) Math.sqrt(xp / 20f - 35);
            }
            default -> {
                return (int) Math.floor(Math.sqrt(xp / 100f));
            }
        }
    }

    public static int calculateXP(int level) {
        return calculateXP(level, "default");
    }

    /**
     * Calculates xp from level with different formulas from other bots (for easier adapting)
     */
    public static int calculateXP(int level, String formula) {
        switch (formula) {
            case "amari" -> {
                return (int) (20 * Math.pow(level, 2) + 35);
            }
            default -> {
                return 100 * (level * level);
            }
        }
    }
}
