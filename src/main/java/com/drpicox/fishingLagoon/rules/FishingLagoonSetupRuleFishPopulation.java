package com.drpicox.fishingLagoon.rules;

import com.drpicox.fishingLagoon.engine.RoundCommands;
import com.drpicox.fishingLagoon.engine.RoundScoresCalculator;
import com.drpicox.fishingLagoon.engine.RoundSeats;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;

public class FishingLagoonSetupRuleFishPopulation implements FishingLagoonSetupRule {


    @Override
    public void setup(RoundScoresCalculator scores, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        var maxDensity = descriptor.getMaxDensity();
        var lagoonCount = seats.getLagoonCount(maxDensity);

        for (int lagoonIndex = 0; lagoonIndex < lagoonCount; lagoonIndex++) {
            var lagoonDescriptor = descriptor.getLagoonDescriptor(lagoonIndex);
            var fishPopulation = lagoonDescriptor.getFishPopulation();
            scores.sumFishPopulation(lagoonIndex, fishPopulation);
        }
    }
}
