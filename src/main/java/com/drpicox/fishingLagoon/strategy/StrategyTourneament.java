package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrategyTourneament {

    private Map<BotId, Strategy> strategies = new HashMap<>();
    private List<LagoonHistory> histories = new ArrayList<>();

    public StrategyTourneament() { }

    public StrategyTourneament addStrategy(String name, Strategy strategy) {
        if (!histories.isEmpty()) throw new IllegalArgumentException("Rounds already starteds");

        strategies.put(new BotId(name), strategy);

        return this;
    }

    public void round(int weekCount, long initialFishCount, String... playingNames) {
        Lagoon initialLagoon = new Lagoon(initialFishCount);
        LagoonHistory history = new LagoonHistory(weekCount, initialLagoon);

        Set<BotId> botIds = Stream.of(playingNames).map(x -> new BotId(x)).collect(Collectors.toSet());
        botIds.retainAll(strategies.keySet());
        if (botIds.isEmpty()) {
            botIds = strategies.keySet();
        }

        for (BotId botId : botIds) {
            Strategy strategy = strategies.get(botId);
            BotId[] competitors = botIds.stream().filter(b -> b != botId).toArray(BotId[]::new);

            Action[] actions = strategy.getOrders(weekCount, initialLagoon, competitors);

            history = history.putActions(botId, actions);
        }

        for (Strategy strategy: strategies.values()) {
            strategy.learnFromHistory(history);
        }

        histories.add(history);
    }

    public List<BotId> getBots() {
        return new ArrayList<>(strategies.keySet());
    }

    public List<LagoonHistory> getHistories() {
        return new ArrayList<>(histories);
    }
}
