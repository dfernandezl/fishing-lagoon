package com.drpicox.fishingLagoon.strategy.drpicox;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static com.drpicox.fishingLagoon.actions.Actions.rest;

public class PercentStrategy extends Strategy {

    private double percent;

    public PercentStrategy(double percent) {
        this.percent = percent;
    }

    @Override
    public int seat(BotId botId, LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    @Override
    public Action[] getOrders(BotId botId, LagoonRound round) {
        List<Action> actions = new ArrayList<>();

        LagoonHistory myImaginaryHistory = round.getLagoonHistoryOf(botId);
        for (int weekIndex = 0; weekIndex < round.getWeekCount(); weekIndex++) {
            Lagoon lagoon = myImaginaryHistory.getLagoonAt(weekIndex);
            long availableFish = lagoon.getLagoonFishCount();

            Action action = fish((long) (availableFish * percent));
            actions.add(action);
            myImaginaryHistory = myImaginaryHistory.putAction(botId, weekIndex, action);
        }

        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void learnFromRound(BotId botId, LagoonRound lagoonRound) {
    }
}
