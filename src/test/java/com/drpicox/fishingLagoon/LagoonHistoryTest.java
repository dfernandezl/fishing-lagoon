package com.drpicox.fishingLagoon;


import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import org.junit.Test;

import java.util.List;

import static com.drpicox.fishingLagoon.actions.Actions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LagoonHistoryTest {

    private static final BotId botId1 = new BotId("bot1");
    private static final BotId botId2 = new BotId("bot2");
    private static final BotId botId3 = new BotId("bot3");
    private static final Lagoon initialLagoon = new Lagoon(9L);

    @Test
    public void lagoon_history_creation() {
        LagoonHistory lagoonHistory = new LagoonHistory(3, initialLagoon);

        Lagoon lagoon0 = lagoonHistory.getLagoonAt(0);
        Lagoon lagoon1 = lagoonHistory.getLagoonAt(1);
        Lagoon lagoon2 = lagoonHistory.getLagoonAt(2);

        assertThat(lagoon0.getLagoonFishCount(), is(9L));
        assertThat(lagoon1.getLagoonFishCount(), is(13L));
        assertThat(lagoon2.getLagoonFishCount(), is(19L));
    }

    @Test
    public void lagoon_history_actions() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(3, initialLagoon);

        LagoonHistory lagoonHistory1 = lagoonHistory0.putActions(botId1, fish(7L), rest(), fish(2L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(28L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon1.getFishCountOf(botId1), is(9L));
    }

    @Test
    public void lagoon_history_actions_can_be_changed_individually() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(3, initialLagoon)
                .putActions(botId1, fish(7L), rest(), fish(2L));
        LagoonHistory lagoonHistory1 = lagoonHistory0
                .putAction(botId1,0, fish(6L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon0.getFishCountOf(botId1), is(9L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(6L));
        assertThat(finalLagoon1.getFishCountOf(botId1), is(8L));
    }

    @Test
    public void lagoon_history_actions_can_be_setted_individually() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(3, initialLagoon)
                .putActions(botId1, fish(7L), rest(), fish(2L));
        LagoonHistory lagoonHistory1 = lagoonHistory0
                .putAction(botId2,0, fish(6L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon0.getFishCountOf(botId1), is(9L));
        assertThat(finalLagoon0.getFishCountOf(botId2), is(0L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(0L));
        assertThat(finalLagoon1.getFishCountOf(botId1), is(3L));
        assertThat(finalLagoon1.getFishCountOf(botId2), is(6L));
    }

    @Test
    public void lagoon_weekCount_fishes_stop_reproducing_after_limit_reached() {
        LagoonHistory lagoonHistoy = new LagoonHistory(3, initialLagoon);

        Lagoon lagoon4 = lagoonHistoy.getLagoonAt(4);

        assertThat(lagoonHistoy.getWeekCount(), is(3));
        assertThat(lagoon4.getLagoonFishCount(), is(28L));
    }

    @Test
    public void lagoon_weekCount_stops_accepting_orders() {
        LagoonHistory lagoonHistoy = new LagoonHistory(3, initialLagoon)
                .putAction(botId1, 3, fish(99));

        Lagoon lagoon3 = lagoonHistoy.getLagoonAt(3);

        assertThat(lagoon3.getLagoonFishCount(), is(28L));
        assertThat(lagoon3.getFishCountOf(botId1), is(0L));
        assertThat(lagoon3.getActionOf(botId1), is(nullValue()));
    }

    @Test
    public void lagoon_get_bots() {
        LagoonHistory history = new LagoonHistory(3, initialLagoon)
                .putAction(botId1, 0, fish(1))
                .putAction(botId1, 1, fish(2))
                .putAction(botId2, 1, fish(1));

        List<BotId> bots = history.getBots();

        assertThat(bots, containsInAnyOrder(botId1, botId2));
    }

    @Test
    public void lagoon_get_initial_lagoon() {
        LagoonHistory history = new LagoonHistory(3, initialLagoon)
                .putAction(botId1, 0, fish(1))
                .putAction(botId1, 1, fish(2))
                .putAction(botId2, 1, fish(1));


        Lagoon lagoonI = history.getInitialLagoon();
        Lagoon lagoon0 = history.getLagoonAt(0);

        assertThat(lagoonI.getLagoonFishCount(), is(initialLagoon.getLagoonFishCount()));
        assertThat(lagoonI.getFishCountOf(botId1), is(initialLagoon.getFishCountOf(botId1)));
        assertThat(lagoon0.getLagoonFishCount(), is(initialLagoon.getLagoonFishCount()));
        assertThat(lagoon0.getFishCountOf(botId1), is(initialLagoon.getFishCountOf(botId1)));
    }

    @Test
    public void lagoon_get_actions() {
        LagoonHistory history = new LagoonHistory(3, initialLagoon)
                .putAction(botId1, 0, fish(1))
                .putAction(botId1, 1, fish(2))
                .putAction(botId2, 1, fish(1));

        List<Action> actions = history.getActionsOf(botId1);

        assertThat(actions, contains(fish(1), fish(2), null));
    }

    @Test
    public void lagoon_get_fish_count() {
        LagoonHistory history = new LagoonHistory(3, initialLagoon)
                .putAction(botId1, 0, fish(1))
                .putAction(botId1, 1, fish(2))
                .putAction(botId1, 2, fish(1))
                .putAction(botId2, 1, fish(1));

        List<Long> fishCounts = history.getFishCountsOf(botId1);
        long fishCount = history.getFishCountOf(botId1);

        assertThat(fishCounts, contains(0L, 1L, 3L, 4L));
        assertThat(fishCount, is(4L));
    }
}
