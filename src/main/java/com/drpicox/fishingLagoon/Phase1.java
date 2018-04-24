package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.*;

import java.util.List;

public class Phase1 {

    public static void main(String... args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        StrategyTourneament tourneament = new StrategyTourneament()
                .addStrategy("rest", new RestStrategy())
                .addStrategy("one", new OneStrategy())
                .addStrategy("power", new PowerStrategy())
                .addStrategy("pct10", new PercentStrategy(0.10))
                .addStrategy("pct40", new PercentStrategy(0.40));

        tourneament.round(10, 19L);
        tourneament.round(10, 19L, "rest", "power");
        tourneament.round(10, 39L);
        tourneament.round(new LagoonRound(10).addLagoons(new Lagoon(19L), new Lagoon(39L)));

        printResults(tourneament);
    }



    private static void printResults(StrategyTourneament tourneament) {
        List<BotId> bots = tourneament.getBots();

        for (BotId botId: bots) {
            System.out.print(botId.toString());
            for (LagoonRound round : tourneament.getRounds()) {
                long count = round.getScoreOf(botId);
                System.out.print(";"+count);
            }
            System.out.println();
        }
    }

    private static void printTournamentDetails(StrategyTourneament tourneament) {
        List<BotId> bots = tourneament.getBots();

        System.out.print("Round");
        iterate(tourneament, (roundIndex, weekIndex) -> {
            System.out.print(";"+roundIndex);
        });
        System.out.println();

        System.out.print("Lagoons");
        iterate(tourneament, (roundIndex, weekIndex) -> {
            System.out.print(";"+tourneament.getRound(roundIndex).getLagoonHistories().size());
        });
        System.out.println();

        System.out.print("Week");
        iterate(tourneament, (roundIndex, weekIndex) -> {
            System.out.print(";"+weekIndex);
        });
        System.out.println();

        System.out.print("Fishes");
        iterate(tourneament, (roundIndex, weekIndex) -> {
            System.out.print(";");
            String[] fishCounts = tourneament.getRound(roundIndex).getLagoonHistories().stream()
                    .map(lagoonHistory -> "" + lagoonHistory.getLagoonAt(weekIndex).getLagoonFishCount())
                    .toArray(String[]::new);
            System.out.print(String.join(",", fishCounts));
        });
        System.out.println();

        bots.forEach(botId -> {
            System.out.print(botId + " lagoon");
            iterate(tourneament, (roundIndex, weekIndex) -> {
                Lagoon lagoon = tourneament.getLagoon(roundIndex, botId, weekIndex);
                System.out.print(";");
                if (lagoon != null) {
                    System.out.print(tourneament.getRound(roundIndex).getLagoonIndexOf(botId));
                }
            });
            System.out.println();

            System.out.print(botId + " score");
            iterate(tourneament, (roundIndex, weekIndex) -> {
                Lagoon lagoon = tourneament.getLagoon(roundIndex, botId, weekIndex);
                System.out.print(";");
                if (lagoon != null) {
                    System.out.print(lagoon.getScoreOf(botId));
                }
            });
            System.out.println();
        });
    }

    private static void iterate(StrategyTourneament tourneament, ResultConsumer consumer) {
        for (int roundIndex = 0; roundIndex < tourneament.getRoundCound(); roundIndex++) {
            int weekCount = tourneament.getWeekCountAt(roundIndex, 0);
            for (int weekIndex = 0; weekIndex <= weekCount; weekIndex++) {
                consumer.accept(roundIndex, weekIndex);
            }
        }
    }

    private static interface ResultConsumer {
        void accept(int roundIndex, int weekIndex);
    }
}
