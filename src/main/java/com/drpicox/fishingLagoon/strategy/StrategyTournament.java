package com.drpicox.fishingLagoon.strategy;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrategyTournament {

    private Map<BotId, Strategy> strategies = new HashMap<>();
    private List<LagoonRound> rounds = new ArrayList<>();
    private List<LagoonHistory> histories = new ArrayList<>();

    public StrategyTournament() { }

    public StrategyTournament addStrategy(String name, Strategy strategy) {
        if (!histories.isEmpty()) throw new IllegalArgumentException("Rounds already starteds");

        BotId botId = new BotId(name);
        strategies.put(botId, strategy);

        return this;
    }

    public void round(LagoonRound round, String... playingNames) {
        Set<BotId> botIds = Stream.of(playingNames).map(x -> new BotId(x)).collect(Collectors.toSet());
        botIds.retainAll(strategies.keySet());
        if (botIds.isEmpty()) {
            botIds.addAll(round.getCompetitors());
        }
        if (botIds.isEmpty()) {
            botIds = strategies.keySet();
        }
        round = round.clearCompetitors().addCompetitors(botIds.stream().toArray(BotId[]::new));

        LagoonRound seatRound = round;
        for (int i = 0; i < 10; i++) {
            for (BotId botId : botIds) {
                Strategy strategy = strategies.get(botId);
                int lagoonIndex = strategy.seat(botId, seatRound);
                seatRound = seatRound.seatBotAt(botId, lagoonIndex);
            }
        }

        LagoonRound actionRound = seatRound;
        for (BotId botId: round.getCompetitors()) {
            Strategy strategy = strategies.get(botId);
            Action[] actions = strategy.getOrders(botId, seatRound);
            actionRound = actionRound.putBotActions(botId, actions);
        }

        LagoonRound finishedRound = actionRound;
        for (BotId botId : strategies.keySet()) {
            Strategy strategy = strategies.get(botId);
            strategy.learnFromRound(botId, finishedRound);
        }

        rounds.add(finishedRound);
        histories.addAll(finishedRound.getLagoonHistories());
    }


    public void round(int weekCount, long initialFishCount, String... playingNames) {
        LagoonRound round = new LagoonRound(weekCount)
                .addLagoons(new Lagoon(initialFishCount));

        round(round, playingNames);
    }

    public List<BotId> getBots() {
        return new ArrayList<>(strategies.keySet());
    }

    public List<LagoonHistory> getHistories() {
        return new ArrayList<>(histories);
    }

    public List<LagoonRound> getRounds() {
        return new ArrayList<>(rounds);
    }

    public int getRoundCound() {
        return rounds.size();
    }

    public LagoonRound getRound(int roundIndex) {
        return rounds.get(roundIndex);
    }

    public int getLagoonCountAt(int roundIndex) {
        return getRound(roundIndex).getLagoonCount();
    }

    public LagoonHistory getLagoonHistory(int roundIndex, int lagoonIndex) {
        return getRound(roundIndex).getLagoonHistory(lagoonIndex);
    }

    public int getWeekCountAt(int roundIndex, int lagoonIndex) {
        return getLagoonHistory(roundIndex, lagoonIndex).getWeekCount();
    }

    public Lagoon getLagoon(int roundIndex, int lagoonIndex, int weekIndex) {
        if (lagoonIndex < 0) return null;
        return getLagoonHistory(roundIndex, lagoonIndex).getLagoonAt(weekIndex);
    }

    public Lagoon getLagoon(int roundIndex, BotId botId, int weekIndex) {
        int lagoonIndex = getRound(roundIndex).getLagoonIndexOf(botId);
        return getLagoon(roundIndex, lagoonIndex, weekIndex);
    }
}
