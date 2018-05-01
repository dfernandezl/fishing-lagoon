package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.RestAction;
import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;

public class RoundCommands {

    private Map<BotId, List<Action>> botCommands = new HashMap<>();

    RoundCommands() {
    }

    RoundCommands(RoundCommands sample) {
        botCommands.putAll(sample.botCommands);
    }

    public Set<BotId> getBots() {
        return new HashSet<>(botCommands.keySet());
    }

    public Object getBotsCount() {
        return botCommands.size();
    }

    public List<Action> get(BotId botId) {
        return new ArrayList<>(botCommands.get(botId));
    }

    public Action getAction(BotId botId, int weekIndex) {
        var botActions = botCommands.get(botId);
        if (botActions == null) return RestAction.DEFAULT;

        return botActions.get(weekIndex);
    }

    public LagoonWeekCommandsView getLagoonWeekView(Integer lagoonIndex, RoundSeats seats, int weekIndex) {
        return new LagoonWeekCommandsView(seats.getLagoonBots(lagoonIndex), weekIndex, this);
    }

    boolean commandBot(BotId botId, List<Action> actions) {
        botCommands.put(botId, new ArrayList<>(actions));
        return true;
    }

    public void forceCommandBot(BotId botId, List<Action> actions) {
        botCommands.put(botId, new ArrayList<>(actions));
    }

    public Map<String,Object> toMap() {
        var result = new HashMap<String,Object>();
        for (var bot: botCommands.keySet()) {
            var actions = botCommands.get(bot);
            var list = new ArrayList<String>();
            for (var action: actions) {
                list.add(action.toString());
            }

            var botMap = new LinkedHashMap<String,Object>();
            botMap.put("actions", list);
            result.put(bot.getValue(), botMap);
        }
        return result;
    }
}
