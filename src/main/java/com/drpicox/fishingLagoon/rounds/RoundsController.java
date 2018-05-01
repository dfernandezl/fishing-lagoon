package com.drpicox.fishingLagoon.rounds;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.common.IdGenerator;
import com.drpicox.fishingLagoon.common.SequenceIdGenerator;
import com.drpicox.fishingLagoon.common.TimeOffset;
import com.drpicox.fishingLagoon.common.TimeStamp;
import com.drpicox.fishingLagoon.engine.RoundEngine;
import com.drpicox.fishingLagoon.parser.RoundDescriptor;
import com.drpicox.fishingLagoon.parser.RoundParser;
import com.drpicox.fishingLagoon.rules.FishingLagoonRules;

import java.sql.SQLException;
import java.util.List;

public class RoundsController {
    private static final long MIN_ROUND_DURATION = 60000L;

    private IdGenerator idGenerator;
    private FishingLagoonRules fishingLagoonRules;
    private RoundsCommandsStore roundsCommandsStore;
    private RoundsDescriptorsStore roundsDescriptorsStore;
    private RoundsSeatsStore roundsSeatsStore;
    private RoundsStore roundsStore;

    public RoundsController(IdGenerator idGenerator, FishingLagoonRules fishingLagoonRules, RoundsCommandsStore roundsCommandsStore, RoundsDescriptorsStore roundsDescriptorsStore, RoundsSeatsStore roundsSeatsStore, RoundsStore roundsStore) {
        this.idGenerator = idGenerator;
        this.fishingLagoonRules = fishingLagoonRules;
        this.roundsCommandsStore = roundsCommandsStore;
        this.roundsDescriptorsStore = roundsDescriptorsStore;
        this.roundsSeatsStore = roundsSeatsStore;
        this.roundsStore = roundsStore;
    }

    public Round create(RoundDescriptor roundDescriptor, TimeStamp ts) throws SQLException {
        if (roundDescriptor.getTotalMilliseconds() < MIN_ROUND_DURATION) {
            throw new IllegalArgumentException("Round too fast");
        }

        Round activeRound = getActiveRound(ts);
        if (activeRound != null) throw new IllegalArgumentException("There is an active round");

        var roundId = new RoundId(idGenerator.next());
        var roundDuration = new TimeOffset(roundDescriptor.getTotalMilliseconds());
        Round round = new Round(roundId, ts, roundDuration);

        roundsDescriptorsStore.create(round, roundDescriptor);
        roundsStore.save(round);

        return round;
    }

    private Round getActiveRound(TimeStamp ts) throws SQLException {
        Round activeRound = null;
        var sinceRounds = roundsStore.listSince(ts);
        for (var round: sinceRounds) {
            if (round.isActive(ts)) {
                activeRound = round;
            }
        }
        return activeRound;
    }

    public List<Round> list() throws SQLException {
        return roundsStore.list();
    }

    public Round getRound(RoundId id, TimeStamp ts) throws SQLException {
        var round = roundsStore.get(id);
        var engine = getRoundEngine(round);

        round.apply(engine, fishingLagoonRules, ts);
        return round;
    }

    private RoundEngine getRoundEngine(Round round) throws SQLException {
        var engine = roundsDescriptorsStore.get(round);
        roundsSeatsStore.get(round.getId(), engine);
        roundsCommandsStore.get(round.getId(), engine);
        return engine;
    }

    public Round seatBot(RoundId id, BotId botId, int lagoonIndex, TimeStamp ts) throws SQLException {
        var round = roundsStore.get(id);
        if (!round.isActive(ts)) return round;

        var engine = getRoundEngine(round);
        if (engine.getTimeState(ts).isAcceptingSeats()) {
            if (engine.seatBot(botId, lagoonIndex)) {
                roundsSeatsStore.save(round.getId(), botId, lagoonIndex);
            }
        }

        return getRound(id, ts);
    }

    public Round commandBot(RoundId id, BotId botId, List<Action> actions, TimeStamp ts) throws SQLException {
        var round = roundsStore.get(id);
        if (!round.isActive(ts)) return round;

        var engine = getRoundEngine(round);
        if (engine.getTimeState(ts).isAcceptingCommands()) {
            if (engine.commandBot(botId, actions)) {
                roundsCommandsStore.save(round.getId(), botId, actions);
            }
        }

        return getRound(id, ts);
    }
}
