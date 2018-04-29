package com.drpicox.fishingLagoon.rules;

import com.drpicox.fishingLagoon.engine.RoundCommands;
import com.drpicox.fishingLagoon.engine.RoundScoresCalculator;
import com.drpicox.fishingLagoon.engine.RoundSeats;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;

public class FishingLagoonRuleProcreation implements FishingLagoonRule {

    @Override
    public void apply(int weekIndex, RoundScoresCalculator scores, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        for (var lagoonIndex: scores.getLagoonIndices()) {
            var fishPopulation = scores.getFishPopulation(lagoonIndex);
            scores.sumFishPopulation(lagoonIndex, fishPopulation / 2);
        }
    }
}
