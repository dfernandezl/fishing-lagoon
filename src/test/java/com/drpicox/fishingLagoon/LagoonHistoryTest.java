package com.drpicox.fishingLagoon;


import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import org.junit.Test;

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
        LagoonHistory lagoonHistory = new LagoonHistory(initialLagoon);

        Lagoon lagoon0 = lagoonHistory.getLagoonAt(0);
        Lagoon lagoon1 = lagoonHistory.getLagoonAt(1);
        Lagoon lagoon2 = lagoonHistory.getLagoonAt(2);

        assertThat(lagoon0.getLagoonFishCount(), is(9L));
        assertThat(lagoon1.getLagoonFishCount(), is(13L));
        assertThat(lagoon2.getLagoonFishCount(), is(19L));
    }

    @Test
    public void lagoon_history_actions() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(initialLagoon);

        LagoonHistory lagoonHistory1 = lagoonHistory0.putBotActions(botId1, fish(7L), rest(), fish(2L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(28L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon1.getBotFishCount(botId1), is(9L));
    }

    @Test
    public void lagoon_history_actions_can_be_changed_individually() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(initialLagoon)
                .putBotActions(botId1, fish(7L), rest(), fish(2L));
        LagoonHistory lagoonHistory1 = lagoonHistory0
                .putAction(botId1,0, fish(6L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon0.getBotFishCount(botId1), is(9L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(6L));
        assertThat(finalLagoon1.getBotFishCount(botId1), is(8L));
    }

    @Test
    public void lagoon_history_actions_can_be_setted_individually() {
        LagoonHistory lagoonHistory0 = new LagoonHistory(initialLagoon)
                .putBotActions(botId1, fish(7L), rest(), fish(2L));
        LagoonHistory lagoonHistory1 = lagoonHistory0
                .putAction(botId2,0, fish(6L));

        Lagoon finalLagoon0 = lagoonHistory0.getLagoonAt(3);
        Lagoon finalLagoon1 = lagoonHistory1.getLagoonAt(3);

        assertThat(finalLagoon0.getLagoonFishCount(), is(3L));
        assertThat(finalLagoon0.getBotFishCount(botId1), is(9L));
        assertThat(finalLagoon0.getBotFishCount(botId2), is(0L));
        assertThat(finalLagoon1.getLagoonFishCount(), is(0L));
        assertThat(finalLagoon1.getBotFishCount(botId1), is(3L));
        assertThat(finalLagoon1.getBotFishCount(botId2), is(6L));
    }
}
