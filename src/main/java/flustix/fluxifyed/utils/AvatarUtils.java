package flustix.fluxifyed.utils;

import java.util.Random;

public class AvatarUtils {
    public static String getDefaultAvatar() {
        Random random = new Random();
        int avatar = random.nextInt(5) + 1;
        return "https://cdn.discordapp.com/embed/avatars/" + avatar + ".png";
    }
}
