package com.drpicox.fishingLagoon.rounds;

import com.drpicox.fishingLagoon.common.TimeOffset;
import com.drpicox.fishingLagoon.common.TimeStamp;
import com.drpicox.fishingLagoon.engine.RoundCommands;
import com.drpicox.fishingLagoon.engine.RoundEngine;
import com.drpicox.fishingLagoon.engine.RoundScores;
import com.drpicox.fishingLagoon.engine.RoundSeats;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;
import com.drpicox.fishingLagoon.rules.FishingLagoonRules;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Round {

    private RoundId id;
    private TimeStamp startTs;
    private TimeStamp endTs;
    private String state;
    private Map<String,Object> descriptor;
    private Map<String,Object> seats;
    private Map<String,Object> commands;
    private Map<String,Object> scores;

    public Map<String,Object> toMap() {
        var result = new LinkedHashMap<String,Object>();
        result.put("id", id.getValue());
        result.put("startTs", startTs.getMilliseconds());
        result.put("endTs", endTs.getMilliseconds());
        if (state != null) result.put("state", descriptor);
        if (descriptor != null) result.put("descriptor", descriptor);
        if (seats != null) result.put("seats", seats);
        if (commands != null) result.put("commands", commands);
        if (scores != null) result.put("scores", scores);
        return result;
    }

    public Round(RoundId id, TimeStamp startTs, TimeOffset roundDuration) {
        this.id = id;
        this.startTs = startTs;
        this.endTs = startTs.plus(roundDuration);
    }

    public Round(RoundId id, TimeStamp startTs, TimeStamp endTs) {
        this.id = id;
        this.startTs = startTs;
        this.endTs = endTs;
    }

    public Round(Round round) {
        this.id = round.id;
        this.startTs = round.startTs;
        this.endTs = round.endTs;
    }

    public boolean isActive(TimeStamp ts) {
        return startTs.compareTo(ts) <= 0 && ts.compareTo(endTs) < 0;
    }

    public RoundId getId() {
        return id;
    }

    public TimeStamp getStartTs() {
        return startTs;
    }

    public TimeStamp getEndTs() {
        return endTs;
    }

    public Map<String,Object> getDescriptor() {
        return descriptor;
    }
    public Map<String,Object> getSeats() {
        return seats;
    }
    public Map<String, Object> getCommands() {
        return commands;
    }
    public Map<String, Object> getScores() {
        return scores;
    }

    public void apply(RoundEngine roundEngine, FishingLagoonRules rules, TimeStamp ts) {
        var timeState = roundEngine.getTimeState(ts);

        state = timeState.toString();
        descriptor = null;
        seats = null;
        commands = null;
        scores = null;

        if (timeState.isDescriptorReadable()) {
            descriptor = roundEngine.getDescriptor().toMap();
        }
        if (timeState.isSeatsReadable()) {
            seats = roundEngine.getSeats().toMap();
        }
        if (timeState.isCommandsReadable()) {
            commands = roundEngine.getCommands().toMap();
        }
        if (timeState.isScoresReadable()) {
            scores = roundEngine.getScores(rules).toMap();
        }
    }
}
