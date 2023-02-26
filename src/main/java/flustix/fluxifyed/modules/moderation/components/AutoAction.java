package flustix.fluxifyed.modules.moderation.components;

import flustix.fluxifyed.modules.moderation.types.AutoActionType;

import java.util.List;

public class AutoAction {
    private final AutoActionType action;
    private final int duration; // in seconds, only for timeouts
    private final int infractions;
    private final int threshold; // the timespan for the infractions to be counted in

    public AutoAction(AutoActionType action, int duration, int infractions, int threshold) {
        this.action = action;
        this.duration = duration;
        this.infractions = infractions;
        this.threshold = threshold;
    }

    public boolean canTrigger(List<Infraction> list) {
        if (list.size() < infractions) return false;

        long time = System.currentTimeMillis();
        int count = 0;
        for (Infraction infraction : list) {
            if (time - infraction.getTime() < threshold * 1000L) count++;
        }

        return count >= infractions;
    }

    public AutoActionType getAction() {
        return action;
    }

    public int getDuration() {
        return duration;
    }

    public int getInfractions() {
        return infractions;
    }
}
