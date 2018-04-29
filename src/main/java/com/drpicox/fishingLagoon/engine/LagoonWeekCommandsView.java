package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;

import java.util.HashSet;
import java.util.Set;

public class LagoonWeekCommandsView {
    private Set<BotId> lagoonBots;
    private int weekIndex;
    private RoundCommands roundCommands;

    public LagoonWeekCommandsView(Set<BotId> lagoonBots, int weekIndex, RoundCommands roundCommands) {
        this.lagoonBots = lagoonBots;
        this.weekIndex = weekIndex;
        this.roundCommands = roundCommands;
    }

    public Set<BotId> getBots() {
        return new HashSet<>(lagoonBots);
    }

    public Action getAction(BotId bot) {
        return roundCommands.getAction(bot, weekIndex);
    }
}
