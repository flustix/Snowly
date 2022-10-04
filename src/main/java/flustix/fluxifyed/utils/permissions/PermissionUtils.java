package flustix.fluxifyed.utils.permissions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class PermissionUtils {
    public static boolean checkLevel(Member member, PermissionLevel level) {
        return switch (level) {
            case CREATOR -> member.getUser().getId().equals("386436194709274627");
            case OWNER -> member.isOwner();
            case ADMIN -> member.hasPermission(Permission.ADMINISTRATOR);
            case MODERATOR -> member.hasPermission(Permission.BAN_MEMBERS);
            case EVERYONE -> true;
        };
    }

    public static String getDescription(PermissionLevel level) {
        return switch (level) {
            case CREATOR -> "Bot Creator";
            case OWNER -> "Server Owner";
            case ADMIN -> "Administrator";
            case MODERATOR -> "Moderator (Able to ban people)";
            case EVERYONE -> "Everyone";
        };

    }
}
