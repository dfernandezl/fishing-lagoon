package com.drpicox.fishingLagoon.engine;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.common.TimeStamp;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;
import com.drpicox.fishingLagoon.rules.FishingLagoonRules;

import java.util.List;

import static com.drpicox.fishingLagoon.engine.RoundPhaseState.COMMANDING;
import static com.drpicox.fishingLagoon.engine.RoundPhaseState.SCORING;
import static com.drpicox.fishingLagoon.engine.RoundPhaseState.SEATING;

public class RoundEngine {
    private long startTs;
    private RoundDescriptor roundDescriptor;

    private RoundPhaseState phaseState;
    private RoundSeats seats;
    private RoundCommands commands;
    private RoundScores scores;

    private FishingLagoonRules rules;

    public RoundEngine(TimeStamp startTs, RoundDescriptor roundDescriptor) {
        this(startTs.getMilliseconds(), roundDescriptor);
    }

    public RoundEngine(long startTs, RoundDescriptor roundDescriptor) {
        this.startTs = startTs;
        this.roundDescriptor = roundDescriptor;

        phaseState = SEATING;
        seats = new RoundSeats();
        commands = new RoundCommands();
        scores = null; // computed under demand
    }

    // descriptor

    public RoundDescriptor getDescriptor() {
        return roundDescriptor;
    }

    // time and round state

    public long getStartTs() {
        return startTs;
    }

    public RoundTimeState getTimeState(long ts) {
        return RoundTimeState.get(ts - startTs, roundDescriptor);
    }

    public RoundTimeState getTimeState(TimeStamp ts) {
        return getTimeState(ts.getMilliseconds());
    }

    // round seats

    public boolean seatBot(BotId botId, int lagoonIndex) {
        phaseState.verifySeat();
        if (seats == null) seats = new RoundSeats();

        return seats.seatBot(botId, lagoonIndex, getLagoonCount());
    }

    public void forceSeatBot(BotId botId, int lagoonIndex) {
        seats.forceSeatBot(botId, lagoonIndex);
    }

    public RoundSeats getSeats() {
        return new RoundSeats(seats);
    }


    // lagoon count

    public int getLagoonCount() {
        if (seats == null) return 1;

        var maxDensity = roundDescriptor.getMaxDensity();
        var lagoonCount = seats.getLagoonCount(maxDensity);
        return lagoonCount;
    }

    // round commands

    public boolean commandBot(BotId botId, List<Action> actions) {
        if (actions.size() != getWeekCount()) throw new IllegalArgumentException("Actions length must match weekCount");
        phaseState.verifyCommand();
        phaseState = COMMANDING;

        var lagoonIndex = seats.getBotSeat(botId);
        if (lagoonIndex == -1) return false;

        return commands.commandBot(botId, actions);
    }

    int getWeekCount() {
        return roundDescriptor.getWeekCount();
    }

    public void forceCommandBot(BotId botId, List<Action> actions) {
        commands.forceCommandBot(botId, actions);
    }

    public RoundCommands getCommands() {
        return new RoundCommands(commands);
    }

    // scores

    public RoundScores getScores(FishingLagoonRules rules) {
        phaseState = SCORING;

        // force recompute if rules change, and save new rules
        if (this.rules != rules) {
            this.rules = rules;
            scores = null;
        }

        // compute if not computed, and save
        if (scores == null) {
            scores = rules.score(roundDescriptor, seats, commands);
        }

        return new RoundScores(scores);
    }

    // weeks
}