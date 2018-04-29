package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;

public class RoundScores {
    private Map<Integer, Long> fishPopulations = new HashMap<>();
    private Map<BotId, Long> botScores = new HashMap<>();

    public RoundScores() {
    }

    public RoundScores(RoundScores sample) {
        this.fishPopulations.putAll(sample.fishPopulations);
        this.botScores.putAll(sample.botScores);
    }



    // lagoons

    public int getLagoonCount() {
        return fishPopulations.size();
    }

    public Set<Integer> getLagoonIndices() {
        return new HashSet<>(fishPopulations.keySet());
    }

    public long getFishPopulation(int lagoonIndex) {
        return fishPopulations.getOrDefault(lagoonIndex, 0L);
    }

    void putFishPopulation(int lagoonIndex, long fishPopulation) {
        fishPopulations.put(lagoonIndex, fishPopulation);
    }

    // bots

    public int getBotCount() {
        return botScores.size();
    }

    public long getScore(BotId bot) {
        return botScores.getOrDefault(bot, 0L);
    }

    void putScore(BotId bot, long score) {
        botScores.put(bot, score);
    }

    public Set<BotId> getBots() {
        return new HashSet<>(botScores.keySet());
    }

    public Map<String,Object> toMap() {
        var lagoons = new ArrayList<Object>();
        for (int lagoonIndex = 0; lagoonIndex < getLagoonCount(); lagoonIndex++) {
            var lagoonMap = new LinkedHashMap<String,Object>();
            lagoonMap.put("fishPopulation", getFishPopulation(lagoonIndex));
            lagoons.add(lagoonMap);
        }

        var bots = new HashMap<String,Object>();
        for (var bot: getBots()) {
            var botMap = new LinkedHashMap<String,Object>();
            botMap.put("score", getScore(bot));
            bots.put(bot.getValue(), botMap);
        }

        var result = new LinkedHashMap<String,Object>();
        result.put("lagoons", lagoons);
        result.put("bots", bots);
        return result;
    }
}
