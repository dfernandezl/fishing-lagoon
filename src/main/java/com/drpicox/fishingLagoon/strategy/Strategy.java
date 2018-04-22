package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;

import java.util.List;
import java.util.Random;

public abstract class Strategy {

    protected BotId botId;

    public void ownBotId(BotId botId) {
        this.botId = botId;
    }

    public int seat(LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    public Action[] getOrders(LagoonRound round) {
        Lagoon lagoon = round.getStartingLagoonOf(botId);
        List<BotId> competitors = round.getCompetitors();
        competitors.remove(botId);
        return getOrders(round.getWeekCount(), lagoon, competitors.toArray(new BotId[competitors.size()]));
    }

    @Deprecated
    public Action[] getOrders(int weekCount, Lagoon lagoon, BotId...comptetitors) {
        return new Action[0];
    }

    public void learnFromRound(LagoonRound lagoonRound) {
        LagoonHistory lagoonHistory = lagoonRound.getLagoonHistoryOf(botId);
        if (lagoonHistory != null) {
            learnFromHistory(lagoonHistory);
        }
    }

    public void learnFromHistory(LagoonHistory lagoonHistory) {}

}
