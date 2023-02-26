package flustix.fluxifyed.modules.moderation.automod.types;

public enum AutoModRuleset {
    Nothing,
    Delete,
    Warn,
    WarnDelete;

    public static AutoModRuleset parse(int level) {
        return switch (level) {
            case 1 -> Delete;
            case 2 -> Warn;
            case 3 -> WarnDelete;
            default -> Nothing;
        };
    }
}
