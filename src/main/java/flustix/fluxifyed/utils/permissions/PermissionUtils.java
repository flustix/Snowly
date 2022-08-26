package flustix.fluxifyed.utils.permissions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class PermissionUtils {
    public static boolean checkLevel(Member member, PermissionLevel level) {
        switch (level) {
            case CREATOR:
                return member.getUser().getId().equals("386436194709274627");
            case OWNER:
                return member.isOwner();
            case ADMIN:
                return member.hasPermission(Permission.ADMINISTRATOR);
            case MODERATOR:
                return member.hasPermission(Permission.BAN_MEMBERS);
            case EVERYONE:
                return true;
            default:
                return false;
        }
    }

    public static String getDescription(PermissionLevel level) {
        switch (level) {
            case CREATOR:
                return "Bot Creator";
            case OWNER:
                return "Server Owner";
            case ADMIN:
                return "Administrator";
            case MODERATOR:
                return "Moderator (Able to ban people)";
            case EVERYONE:
                return "Everyone";
        }

        return "";
    }
}
