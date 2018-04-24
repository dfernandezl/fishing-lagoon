package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;

public abstract class Strategy {

    public abstract int seat(BotId botId, LagoonRound round);
    public abstract Action[] getOrders(BotId botId, LagoonRound round);
    public abstract void learnFromRound(BotId botId, LagoonRound lagoonRound);

}
