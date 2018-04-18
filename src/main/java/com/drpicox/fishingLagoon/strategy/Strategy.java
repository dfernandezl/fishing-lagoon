package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;

public abstract class Strategy {

    public abstract Action[] getOrders(int weekCount, Lagoon lagoon, BotId...comptetitors);
    public void learnFromHistory(LagoonHistory lagoonHistory) {}

}
