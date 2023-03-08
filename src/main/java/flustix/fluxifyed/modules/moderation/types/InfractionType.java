package flustix.fluxifyed.modules.moderation.types;

public enum InfractionType {
    NOTE(false),
    WARN(true),
    MUTE(false),
    KICK(false),
    BAN(false);

    final boolean countForAutoActions;

    InfractionType(boolean countForAutoActions) {
        this.countForAutoActions = countForAutoActions;
    }

    public boolean countForAutoActions() {
        return countForAutoActions;
    }

    public static InfractionType fromString(String type) {
        return switch (type) {
            case "note" -> NOTE;
            case "warn" -> WARN;
            case "mute" -> MUTE;
            case "kick" -> KICK;
            case "ban" -> BAN;
            default -> null;
        };
    }

    public String toString() {
        return super.toString().toLowerCase();
    }
}
