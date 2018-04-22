package com.drpicox.fishingLagoon;


import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import org.junit.Test;

import java.util.List;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LagoonRoundTest {

    private static final BotId botId1 = new BotId("bot1");
    private static final BotId botId2 = new BotId("bot2");
    private static final BotId botId3 = new BotId("bot3");

    private static final Lagoon initialLagoon = new Lagoon(5L);

    @Test
    public void lagoon_round_creation() {
        LagoonRound round = new LagoonRound(3).addLagoons(3, initialLagoon);

        assertThat(round.getWeekCount(), is(3));
        assertThat(round.getLagoonCount(), is(3));
        assertThat(round.getStartingLagoon(0).getLagoonFishCount(), is(5L));
        assertThat(round.getStartingLagoon(1).getLagoonFishCount(), is(5L));
        assertThat(round.getStartingLagoon(2).getLagoonFishCount(), is(5L));
    }

    @Test
    public void lagoon_round_creation_one_by_one() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(new Lagoon(5L), new Lagoon(7L))
                .addLagoons(new Lagoon(9L));

        assertThat(round.getLagoonCount(), is(3));
        assertThat(round.getStartingLagoon(0).getLagoonFishCount(), is(5L));
        assertThat(round.getStartingLagoon(1).getLagoonFishCount(), is(7L));
        assertThat(round.getStartingLagoon(2).getLagoonFishCount(), is(9L));
    }

    @Test
    public void lagoon_round_with_bots() {
        LagoonRound round = new LagoonRound(3).addCompetitors(botId1, botId2, botId3);

        assertThat(round.getCompetitors(), containsInAnyOrder(botId1, botId2, botId3));
    }

    @Test
    public void lagoon_round_seat_bots() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(3, initialLagoon)
                .addCompetitors(botId1, botId2, botId3)
                .seatBotAt(botId1, 0)
                .seatBotAt(botId2, 0)
                .seatBotAt(botId3, 2)
                ;

        assertThat(round.getStartingLagoon(0).getBots(), containsInAnyOrder(botId1, botId2));
        assertThat(round.getStartingLagoon(1).getBots(), is(empty()));
        assertThat(round.getStartingLagoon(2).getBots(), containsInAnyOrder(botId3));
    }

    @Test
    public void lagoon_round_get_starting_lagoon_of() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(3, initialLagoon)
                .addCompetitors(botId1, botId2, botId3)
                .seatBotAt(botId1, 0)
                .seatBotAt(botId2, 0)
                .seatBotAt(botId3, 2)
                ;

        assertThat(round.getStartingLagoonOf(botId1).getBots(), containsInAnyOrder(botId1, botId2));
        assertThat(round.getStartingLagoonOf(botId2).getBots(), containsInAnyOrder(botId1, botId2));
        assertThat(round.getStartingLagoonOf(botId3).getBots(), containsInAnyOrder(botId3));
    }

    @Test
    public void lagoon_round_seat_bots_move_already_sat_bots() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(3, initialLagoon)
                .addCompetitors(botId1, botId2, botId3)
                .seatBotAt(botId1, 0)
                .seatBotAt(botId1, 2)
                ;

        assertThat(round.getStartingLagoon(0).getBots(), is(empty()));
        assertThat(round.getStartingLagoon(1).getBots(), is(empty()));
        assertThat(round.getStartingLagoon(2).getBots(), containsInAnyOrder(botId1));
    }

    @Test
    public void lagoon_round_seat_bots_ignore_not_present_bots() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(3, initialLagoon)
                .addCompetitors(botId1)
                .seatBotAt(botId3, 0)
                ;

        assertThat(round.getStartingLagoon(0).getBots(), is(empty()));
        assertThat(round.getStartingLagoon(1).getBots(), is(empty()));
        assertThat(round.getStartingLagoon(2).getBots(), is(empty()));
    }

    @Test
    public void lagoon_round_accept_actions_from_bots() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(3, initialLagoon)
                .addCompetitors(botId1)
                .seatBotAt(botId1, 0)
                .putBotActions(botId1, fish(3), fish(2))
                ;

        LagoonHistory history = round.getLagoonHistory(0);
        Lagoon finalLagoon = history.getFinalLagoon();

        assertThat(round.getScoreOf(botId1), is(5L));
        assertThat(finalLagoon.getLagoonFishCount(), is(1L));
    }

    @Test
    public void lagoon_round_get_history() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(initialLagoon);

        LagoonHistory history = round.getLagoonHistory(0);
        assertThat(history.getWeekCount(), is(3));
        assertThat(history.getLagoonAt(0).getLagoonFishCount(), is(initialLagoon.getLagoonFishCount()));
    }

    @Test
    public void lagoon_round_get_histories() {
        LagoonRound round = new LagoonRound(3)
                .addLagoons(initialLagoon);

        List<LagoonHistory> histories = round.getLagoonHistories();
        assertThat(histories, hasSize(1));
        assertThat(histories.get(0).getWeekCount(), is(3));
    }

}
