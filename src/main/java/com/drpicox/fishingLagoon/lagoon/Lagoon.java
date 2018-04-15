package com.drpicox.fishingLagoon.lagoon;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.FishAction;
import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;
import java.util.stream.Collectors;

public class Lagoon {
    private long lagoonFishCount;
    private Map<BotId, Long> botsFishCount;
    private Map<BotId, BotAction> botsAction;

    public Lagoon(long lagoonFishCount) {
        this(lagoonFishCount, new HashMap<>(), new HashMap<>());
    }

    private Lagoon(long lagoonFishCount, Map<BotId, Long> botsFishCount, Map<BotId, BotAction> botsAction) {
        this.lagoonFishCount = lagoonFishCount;
        this.botsFishCount = botsFishCount;
        this.botsAction = botsAction;
    }

    public Lagoon putAction(BotId botId, Action action) {
        Lagoon result = copy();
        result.botsAction = new HashMap<>(botsAction);
        result.botsAction.put(botId, new BotAction(botId, action));
        return result;
    }

    public Lagoon cleanActions() {
        Lagoon result = copy();
        result.botsAction = new HashMap<>();
        return result;
    }

    public Lagoon commitWeek() {
        return this
            .commitFishing()
            .commitFishReproduction()
            .cleanActions();
    }

    public long getLagoonFishCount() {
        return lagoonFishCount;
    }

    public long getBotFishCount(BotId botId) {
        return botsFishCount.getOrDefault(botId, 0L);
    }

    private Lagoon commitFishing() {
        List<BotAction> fishingBotActions = botsAction.values().stream()
                .filter(ba -> ba.isFishing())
                .collect(Collectors.toList());
        return commitFishing(fishingBotActions, 1);
    }

    private Lagoon commitFishing(List<BotAction> fishingActions, long startFishCount) {
        long minActionFishCount = Long.MAX_VALUE;
        for (BotAction currentAction: fishingActions) {
            long actionFishCount = currentAction.getFishingCount();
            if (startFishCount <= actionFishCount && actionFishCount < minActionFishCount) {
                minActionFishCount = actionFishCount;
            }
        }

        if (minActionFishCount == Long.MAX_VALUE) return this;
        Lagoon result = copy();
        result.botsFishCount = copyBotsFishCount();

        List<BotId> fishingBots = new LinkedList<>();
        for (BotAction fishAction: fishingActions) {
            long actionFishCount = fishAction.getFishingCount();
            if (minActionFishCount == actionFishCount) {
                fishingBots.add(fishAction.botId);
            }
        }

        long share = Math.min(lagoonFishCount / fishingBots.size(), minActionFishCount);
        for (BotId fishingBot : fishingBots) {
            result.botsFishCount.put(fishingBot, getBotFishCount(fishingBot) + share);
        }
        result.lagoonFishCount -= share * fishingBots.size();

        return result.commitFishing(fishingActions, minActionFishCount + 1);
    }


    private Lagoon commitFishReproduction() {
        Lagoon result = copy();
        result.lagoonFishCount = lagoonFishCount + (lagoonFishCount / 2);
        return result;
    }

    private Lagoon copy() {
        return new Lagoon(lagoonFishCount, botsFishCount, botsAction);
    }

    private Map<BotId,Long> copyBotsFishCount() {
        return new HashMap<>(botsFishCount);
    }


    @Override
    public String toString() {
        Set<BotId> bots = new HashSet<>();
        bots.addAll(botsFishCount.keySet());
        bots.addAll(botsAction.keySet());

        return "Lagoon{"
                + "fish [" + lagoonFishCount + "], "
                + String.join(", ", bots.stream().map(
                        b -> b + "[" + getBotFishCount(b) + "]:" + botsAction.get(b)
                ).toArray(String[]::new))
                + "}";
    }

    private final static class BotAction {
        private BotId botId;
        private Action action;

        public BotAction(BotId botId, Action action) {
            this.botId = botId;
            this.action = action;
        }

        public boolean isFishing() {
            return action instanceof FishAction;
        }

        public long getFishingCount() {
            return ((FishAction) action).getFishingCount();
        }
    }
}
