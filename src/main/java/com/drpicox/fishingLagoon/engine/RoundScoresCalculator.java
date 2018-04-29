package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.bots.BotId;

import java.util.*;

public class RoundScoresCalculator {

    private RoundScores scores = new RoundScores();

    public RoundScores getScores() {
        return scores;
    }

    public Set<Integer> getLagoonIndices() {
        return scores.getLagoonIndices();
    }

    public long getFishPopulation(int lagoonIndex) {
        return scores.getFishPopulation(lagoonIndex);
    }

    public void sumFishPopulation(int lagoonIndex, long amount) {
        var fishPopulation = getFishPopulation(lagoonIndex);
        scores.putFishPopulation(lagoonIndex, fishPopulation + amount);
    }


    public long getScore(BotId bot) {
        return scores.getScore(bot);
    }

    public void sumScore(BotId bot, long amount) {
        var score = getScore(bot);
        scores.putScore(bot, score + amount);
    }

    // view

    public LagoonCalculatorScoreView getLagoonView(int lagoonIndex, RoundSeats seats) {
        return new LagoonCalculatorScoreView(lagoonIndex, seats.getLagoonBots(lagoonIndex), this);
    }
}
