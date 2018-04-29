package com.drpicox.fishingLagoon.rules;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.engine.RoundCommands;
import com.drpicox.fishingLagoon.engine.RoundScores;
import com.drpicox.fishingLagoon.engine.RoundScoresCalculator;
import com.drpicox.fishingLagoon.engine.RoundSeats;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;

import java.util.ArrayList;
import java.util.List;

public class FishingLagoonRules {

    List<FishingLagoonSetupRule> setupRules;
    List<FishingLagoonRule> rules;

    public FishingLagoonRules(List<FishingLagoonSetupRule> setupRules, List<FishingLagoonRule> rules) {
        this.setupRules = new ArrayList<>(setupRules);
        this.rules = new ArrayList<>(rules);
    }

    public RoundScores score(RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        var scoresCalculator = new RoundScoresCalculator();

        setup(scoresCalculator, descriptor, seats, commands);
        applyWeeks(scoresCalculator, descriptor, seats, commands);

        return scoresCalculator.getScores();
    }

    private void setup(RoundScoresCalculator scoresCalculator, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        for (var setupRule: setupRules) {
            setupRule.setup(scoresCalculator, descriptor, seats, commands);
        }
    }

    private void applyWeeks(RoundScoresCalculator scoresCalculator, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        var weekCount = descriptor.getWeekCount();

        for (int weekIndex = 0; weekIndex < weekCount; weekIndex++) {
            applyWeek(weekIndex, scoresCalculator, descriptor, seats, commands);
        }
    }

    private void applyWeek(int weekIndex, RoundScoresCalculator scoresCalculator, RoundDescriptor descriptor, RoundSeats seats, RoundCommands commands) {
        for (var rule: rules) {
            rule.apply(weekIndex, scoresCalculator, descriptor, seats, commands);
        }
    }

}
