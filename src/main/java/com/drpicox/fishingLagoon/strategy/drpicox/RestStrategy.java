package com.drpicox.fishingLagoon.strategy.drpicox;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.Strategy;

import java.util.Random;

import static com.drpicox.fishingLagoon.actions.Actions.rest;

public class RestStrategy extends Strategy {

    @Override
    public int seat(BotId botId, LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    @Override
    public Action[] getOrders(BotId botId, LagoonRound round) {
        return new Action[]{ rest() };
    }

    @Override
    public void learnFromRound(BotId botId, LagoonRound lagoonRound) {
    }
}
