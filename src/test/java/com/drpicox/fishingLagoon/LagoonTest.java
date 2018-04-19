package com.drpicox.fishingLagoon;


import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.drpicox.fishingLagoon.actions.Actions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LagoonTest {

    private static final BotId botId1 = new BotId("bot1");
    private static final BotId botId2 = new BotId("bot2");
    private static final BotId botId3 = new BotId("bot3");

    @Test
    public void lagoon_creation() {
        Lagoon lagoon = new Lagoon(5L);

        long fishCount = lagoon.getLagoonFishCount();
        assertThat(fishCount, is(5L));
    }

    @Test
    public void lagoon_procreation() {
        Lagoon lagoon0 = new Lagoon(3L);

        List<Long> fishCounts = new ArrayList<>(10);
        Lagoon currentLagoon = lagoon0;
        for (int weekIndex = 0; weekIndex < 14; weekIndex++) {
            fishCounts.add(currentLagoon.getLagoonFishCount());
            currentLagoon = currentLagoon.commitWeek();
        }

        assertThat(fishCounts, contains(3L, 4L, 6L, 9L, 13L, 19L, 28L, 42L, 63L, 94L, 141L, 211L, 316L, 474L));
        assertThat(currentLagoon, is(not(lagoon0)));
    }

    @Test
    public void lagoon_seat_adds_a_bot() {
        Lagoon lagoon = new Lagoon(9L)
                .addBot(botId1)
                .addBot(botId2);

        assertThat(lagoon.getBots(), containsInAnyOrder(botId1, botId2));
    }

    @Test
    public void lagoon_seat_adds_many_bots() {
        Lagoon lagoon = new Lagoon(9L)
                .addBots(botId1, botId2);

        assertThat(lagoon.getBots(), containsInAnyOrder(botId1, botId2));
    }

    @Test
    public void lagoon_fishing() {
        Lagoon lagoon0 = new Lagoon(9L).addBots(botId1, botId2);

        Lagoon lagoon1 = lagoon0.putAction(botId1, fish(7L));
        Lagoon lagoon2 = lagoon1.commitWeek();

        assertThat(lagoon0.getLagoonFishCount(), is(9L));
        assertThat(lagoon0.getFishCountOf(botId1), is(0L));
        assertThat(lagoon0.getFishCountOf(botId2), is(0L));
        assertThat(lagoon1.getLagoonFishCount(), is(9L));
        assertThat(lagoon1.getFishCountOf(botId1), is(0L));
        assertThat(lagoon1.getFishCountOf(botId2), is(0L));
        assertThat(lagoon2.getLagoonFishCount(), is(3L));
        assertThat(lagoon2.getFishCountOf(botId1), is(7L));
        assertThat(lagoon2.getFishCountOf(botId2), is(0L));
        assertThat(lagoon0, is(not(lagoon1)));
        assertThat(lagoon0, is(not(lagoon2)));
        assertThat(lagoon1, is(not(lagoon2)));
    }

    @Test
    public void lagoon_fishing_ignores_not_added_bots() {
        Lagoon lagoon = new Lagoon(9L)
                .putAction(botId1, fish(9L))
                .commitWeek();

        assertThat(lagoon.getLagoonFishCount(), is(13L));
    }

    @Test
    public void lagoon_fishing_who_ask_for_less_fishes_first() {
        Lagoon lagoon = new Lagoon(9L)
                .addBots(botId1, botId2, botId3)
                .putAction(botId1, fish(9L))
                .putAction(botId2, fish(7L))
                .putAction(botId3, fish(4L))
                .commitWeek();

        assertThat(lagoon.getLagoonFishCount(), is(0L));
        assertThat(lagoon.getFishCountOf(botId1), is(0L));
        assertThat(lagoon.getFishCountOf(botId2), is(5L));
        assertThat(lagoon.getFishCountOf(botId3), is(4L));
    }

    @Test
    public void lagoon_fishing_share_available_fish_between_same_amount_fishing_count() {
        Lagoon lagoon = new Lagoon(9L)
                .addBots(botId1, botId2)
                .putAction(botId1, fish(9L))
                .putAction(botId2, fish(9L))
                .commitWeek();

        assertThat(lagoon.getLagoonFishCount(), is(1L));
        assertThat(lagoon.getFishCountOf(botId1), is(4L));
        assertThat(lagoon.getFishCountOf(botId2), is(4L));
    }

    @Test
    public void lagoon_action_rest() {
        Lagoon lagoon = new Lagoon(9L)
                .addBots(botId1)
                .putAction(botId1, rest())
                .commitWeek();

        assertThat(lagoon.getLagoonFishCount(), is(13L));
        assertThat(lagoon.getFishCountOf(botId1), is(0L));
    }
}
