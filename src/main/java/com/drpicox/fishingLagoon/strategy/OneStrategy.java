package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;

import java.util.stream.IntStream;

import static com.drpicox.fishingLagoon.actions.Actions.fish;

public class OneStrategy extends Strategy {

    @Override
    public Action[] getOrders(int weekCount, Lagoon lagoon, BotId... comptetitors) {
        return IntStream.range(0, weekCount - 1).mapToObj(x -> fish(1)).toArray(Action[]::new);
    }
}
