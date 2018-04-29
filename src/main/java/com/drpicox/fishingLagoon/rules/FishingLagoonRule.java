package com.drpicox.fishingLagoon.rules;

import com.drpicox.fishingLagoon.engine.RoundCommands;
import com.drpicox.fishingLagoon.engine.RoundScoresCalculator;
import com.drpicox.fishingLagoon.engine.RoundSeats;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;

public interface FishingLagoonRule {

    void apply(int weekIndex, RoundScoresCalculator scores, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands);

}
