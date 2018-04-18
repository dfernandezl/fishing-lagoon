package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;

import static com.drpicox.fishingLagoon.actions.Actions.rest;

public class RestStrategy extends Strategy {

    @Override
    public Action[] getOrders(int weekCount, Lagoon lagoon, BotId... comptetitors) {
        return new Action[]{rest()};
    }
}
