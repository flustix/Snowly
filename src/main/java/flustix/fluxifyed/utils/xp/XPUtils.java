package flustix.fluxifyed.utils.xp;

public class XPUtils {
    public static int calculateLevel(int xp) {
        return (int) Math.floor(Math.sqrt(xp / 100));
    }

    public static int calculateXP(int level) {
        return 100 * (level * level);
    }
}
