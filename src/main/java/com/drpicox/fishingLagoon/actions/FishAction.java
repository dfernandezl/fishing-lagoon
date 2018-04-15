package com.drpicox.fishingLagoon.actions;

public class FishAction extends Action {

    private long fishingCount;

    public FishAction(long fishingCount) {
        super();

        this.fishingCount = fishingCount;
    }

    public long getFishingCount() {
        return fishingCount;
    }

    @Override
    public String toString() {
        return "fish " + fishingCount;
    }
}
