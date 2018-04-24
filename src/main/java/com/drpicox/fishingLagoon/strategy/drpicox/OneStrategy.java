package com.drpicox.fishingLagoon.strategy.drpicox;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.drpicox.fishingLagoon.actions.Actions.fish;

public class OneStrategy extends Strategy {

    @Override
    public int seat(BotId botId, LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    @Override
    public Action[] getOrders(BotId botId, LagoonRound round) {
        List<Action> actions = new ArrayList<>();

        for (int weekIndex = 0; weekIndex < round.getWeekCount(); weekIndex++) {
            actions.add(fish(1));
        }

        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void learnFromRound(BotId botId, LagoonRound lagoonRound) {
    }

}
