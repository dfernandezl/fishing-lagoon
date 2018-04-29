package com.drpicox.fishingLagoon.engine;

public enum RoundPhaseState {

    SEATING(true, true),
    COMMANDING(false, true),
    SCORING(false, false);

    private boolean acceptsSeat;
    private boolean acceptsCommand;

    RoundPhaseState(boolean acceptsSeat, boolean acceptsCommand) {
        this.acceptsSeat = acceptsSeat;
        this.acceptsCommand = acceptsCommand;
    }

    public void verifySeat() {
        if (!acceptsSeat) throw new IllegalStateException("Cannot seat bots when " + this);
    }

    public void verifyCommand() {
        if (!acceptsCommand) throw new IllegalStateException("Cannot command bots when " + this);
    }
}
