package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.bots.BotId;

import java.util.Set;

public class LagoonCalculatorScoreView {
    private int lagoonIndex;
    private Set<BotId> lagoonBots;
    private RoundScoresCalculator scoresCalculator;

    public LagoonCalculatorScoreView(int lagoonIndex, Set<BotId> lagoonBots, RoundScoresCalculator scoresCalculator) {
        this.lagoonIndex = lagoonIndex;
        this.lagoonBots = lagoonBots;
        this.scoresCalculator = scoresCalculator;
    }

    public long getFishPopulation() {
        return scoresCalculator.getFishPopulation(lagoonIndex);
    }

    public void sumFishPopulation(long amount) {
        scoresCalculator.sumFishPopulation(lagoonIndex, amount);
    }

    public void sumScore(BotId bot, long share) {
        scoresCalculator.sumScore(bot, share);
    }
}
