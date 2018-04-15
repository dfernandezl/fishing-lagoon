package com.drpicox.fishingLagoon.lagoon;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;

public class LagoonHistory {

    private List<Lagoon> lagoonsByWeek;
    private Map<String, BotWeekAction> botWeekActions;

    public LagoonHistory(Lagoon initialLagoon) {
        this(new LinkedList<>(), new HashMap<>());

        lagoonsByWeek.add(initialLagoon);
    }

    private LagoonHistory(List<Lagoon> lagoonsByWeek, Map<String, BotWeekAction> botWeekActions) {
        this.lagoonsByWeek = lagoonsByWeek;
        this.botWeekActions = botWeekActions;
    }

    public Lagoon getLagoonAt(int weekIndex) {
        cacheMissingLagoonsUntil(weekIndex);
        return lagoonsByWeek.get(weekIndex);
    }

    public LagoonHistory putAction(BotId botId, int weekIndex, Action action) {
        BotWeekAction bwa = new BotWeekAction(botId, weekIndex, action);

        LagoonHistory result = copy();
        result.botWeekActions = new HashMap<>(botWeekActions);
        result.botWeekActions.put(bwa.key, bwa);
        result.lagoonsByWeek = result.copyLagoonsByWeekUntil(weekIndex);
        return result;
    }

    public LagoonHistory putBotActions(BotId botId, Action... actions) {
        LagoonHistory result = copy();
        result.botWeekActions = new HashMap<>(botWeekActions);
        for (int weekIndex = 0; weekIndex < actions.length; weekIndex++) {
            BotWeekAction bwa = new BotWeekAction(botId, weekIndex, actions[weekIndex]);
            result.botWeekActions.put(bwa.key, bwa);
        }
        result.botWeekActions.values().removeIf(bwa -> bwa.weekIndex >= actions.length);
        result.lagoonsByWeek = copyLagoonsByWeekUntil(0);
        return result;
    }

    private void cacheMissingLagoonsUntil(int targetWeekIndex) {
        int weekIndex = lagoonsByWeek.size();
        while (weekIndex - 1 <= targetWeekIndex) {

            int prevWeekIndex = weekIndex - 1;
            Lagoon prevLagoon = lagoonsByWeek.get(prevWeekIndex);
            prevLagoon = prevLagoon.cleanActions();
            prevLagoon = applyActionsAt(prevLagoon, prevWeekIndex);
            lagoonsByWeek.set(prevWeekIndex, prevLagoon);

            if (weekIndex <= targetWeekIndex) {
                Lagoon currentLagoon = prevLagoon.commitWeek();
                lagoonsByWeek.add(currentLagoon);
            }

            weekIndex = weekIndex + 1;
        }
    }

    private Lagoon applyActionsAt(Lagoon currentLagoon, int weekIndex) {
        for (BotWeekAction bwe: botWeekActions.values()) {
            if (bwe.weekIndex == weekIndex) {
                currentLagoon = currentLagoon.putAction(bwe.botId, bwe.action);
            }
        }

        return currentLagoon;
    }

    private List<Lagoon> copyLagoonsByWeekUntil(int weekIndex) {
        return new ArrayList<>(lagoonsByWeek.subList(0, Math.min(weekIndex + 1, lagoonsByWeek.size())));
    }

    private LagoonHistory copy() {
        return new LagoonHistory(lagoonsByWeek, botWeekActions);
    }

    @Override
    public String toString() {
        return "LagoonHistory{\n  "
                + String.join("\n  ", lagoonsByWeek.stream().map(x -> x.toString()).toArray(String[]::new))
                + "\n}";
    }


    private static final class BotWeekAction {
        final String key;
        final BotId botId;
        final int weekIndex;
        final Action action;

        public BotWeekAction(BotId botId, int weekIndex, Action action) {
            this.key = botId.toString() + "#" + weekIndex;
            this.botId = botId;
            this.weekIndex = weekIndex;
            this.action = action;
        }
    }
}
