package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;

public class RoundSeats {

    private Map<BotId, Integer> botSeats = new HashMap<>();

    public RoundSeats() {
    }

    RoundSeats(RoundSeats sample) {
        botSeats.putAll(sample.botSeats);
    }


    public int getBotCount() {
        return botSeats.size();
    }

    public Set<BotId> getBots() {
        return new HashSet<>(botSeats.keySet());
    }


    public Set<Integer> getLagoonIndices() {
        return new HashSet<>(botSeats.values());
    }


    public Set<BotId> getLagoonBots(int lagoonIndex) {
        Set<BotId> result = new HashSet<>();
        for (var bot: botSeats.keySet()) {
            var botSeat = botSeats.get(bot);
            if (botSeat == lagoonIndex) {
                result.add(bot);
            }
        }
        return result;
    }

    public int getBotSeat(BotId botId) {
        var botSeat = botSeats.get(botId);
        if (botSeat == null) return -1;

        return botSeat;
    }

    public int getLagoonCount(BotId botId, double maxDensity) {
        var botCount = getBotCount();
        var plusOne = botId == null || botSeats.containsKey(botId) ? 0 : 1;
        var result = (int)Math.ceil((botCount + plusOne) / maxDensity);
        return result;
    }


    boolean seatBot(BotId botId, int lagoonIndex, int lagoonCount) {
        var prevBotSeat = getBotSeat(botId);
        if (prevBotSeat == lagoonIndex) return false;

        if (lagoonIndex >= lagoonCount) return false;

        botSeats.put(botId, lagoonIndex);
        return true;
    }

    public void forceSeatBot(BotId botId, int lagoonIndex) {
        botSeats.put(botId, lagoonIndex);
    }

    public Map<String,Object> toMap() {
        var result = new HashMap<String,Object>();
        for (var bot: botSeats.keySet()) {
            var seat = new LinkedHashMap<String, Object>();
            seat.put("lagoonIndex", botSeats.get(bot));
            result.put(bot.getValue(), seat);
        }
        return result;
    }
}
