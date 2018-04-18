package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.strategy.OneStrategy;
import com.drpicox.fishingLagoon.strategy.PowerStrategy;
import com.drpicox.fishingLagoon.strategy.RestStrategy;
import com.drpicox.fishingLagoon.strategy.StrategyTourneament;

import java.util.List;

public class Phase1 {

    public static void main(String... args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        StrategyTourneament tourneament = new StrategyTourneament()
                .addStrategy("rest", new RestStrategy())
                .addStrategy("one", new OneStrategy())
                .addStrategy("power", new PowerStrategy());

        tourneament.round(50, 19L);
        tourneament.round(10, 19L, "rest", "power");
        tourneament.round(10, 39L);

        printResults(tourneament);
    }

    private static void printResults(StrategyTourneament tourneament) {
        List<BotId> bots = tourneament.getBots();

        tourneament.getHistories().forEach(history -> {
            Lagoon initial = history.getInitialLagoon();
            System.out.print(";" + initial.getLagoonFishCount());
        });
        System.out.println();
        bots.forEach(botId -> {
            System.out.print(botId.toString());
            tourneament.getHistories().forEach(history -> {
                long count = history.getFishCountOf(botId);
                System.out.print(";"+count);
            });
            System.out.println();
        });
    }
}
