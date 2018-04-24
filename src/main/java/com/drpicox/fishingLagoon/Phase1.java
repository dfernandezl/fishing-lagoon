package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.*;
import com.drpicox.fishingLagoon.strategy.drpicox.OneStrategy;
import com.drpicox.fishingLagoon.strategy.drpicox.PercentStrategy;
import com.drpicox.fishingLagoon.strategy.drpicox.PowerStrategy;
import com.drpicox.fishingLagoon.strategy.drpicox.RestStrategy;

import java.util.List;

public class Phase1 {

    public static void main(String... args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        tournament1();
        System.out.println("-----");
        tournament2();
    }

    private static void tournament1() {
        StrategyTournament tournament = new StrategyTournament()
                .addStrategy("rest", new RestStrategy())
                .addStrategy("one", new OneStrategy())
                .addStrategy("power", new PowerStrategy())
                .addStrategy("pct10", new PercentStrategy(0.10))
                .addStrategy("pct40", new PercentStrategy(0.40));

        tournament.round(10, 19L);
        tournament.round(10, 19L, "rest", "power");
        tournament.round(10, 39L);
        tournament.round(
                new LagoonRound(10)
                        .addLagoons(new Lagoon(19L))
                        .addLagoons(new Lagoon(39L))
        );

        printResults(tournament);
    }

    private static void tournament2() {
        StrategyTournament tournament = new StrategyTournament()
                .addStrategy("one", new OneStrategy())
                .addStrategy("pct30", new PercentStrategy(0.30));

        tournament.round(10, 19L);
        tournament.round(10, 39L);
        tournament.round(new LagoonRound(10).addLagoons(new Lagoon(19L), new Lagoon(39L)));

        printResults(tournament);
    }


    private static void printResults(StrategyTournament tournament) {
        List<BotId> bots = tournament.getBots();

        for (BotId botId: bots) {
            System.out.print(botId.toString());
            for (LagoonRound round : tournament.getRounds()) {
                long count = round.getScoreOf(botId);
                System.out.print(";"+count);
            }
            System.out.println();
        }
    }

    private static void printTournamentDetails(StrategyTournament tournament) {
        List<BotId> bots = tournament.getBots();

        System.out.print("Round");
        iterate(tournament, (roundIndex, weekIndex) -> {
            System.out.print(";"+roundIndex);
        });
        System.out.println();

        System.out.print("Lagoons");
        iterate(tournament, (roundIndex, weekIndex) -> {
            System.out.print(";"+tournament.getRound(roundIndex).getLagoonHistories().size());
        });
        System.out.println();

        System.out.print("Week");
        iterate(tournament, (roundIndex, weekIndex) -> {
            System.out.print(";"+weekIndex);
        });
        System.out.println();

        System.out.print("Fishes");
        iterate(tournament, (roundIndex, weekIndex) -> {
            System.out.print(";");
            String[] fishCounts = tournament.getRound(roundIndex).getLagoonHistories().stream()
                    .map(lagoonHistory -> "" + lagoonHistory.getLagoonAt(weekIndex).getLagoonFishCount())
                    .toArray(String[]::new);
            System.out.print(String.join(",", fishCounts));
        });
        System.out.println();

        bots.forEach(botId -> {
            System.out.print(botId + " lagoon");
            iterate(tournament, (roundIndex, weekIndex) -> {
                Lagoon lagoon = tournament.getLagoon(roundIndex, botId, weekIndex);
                System.out.print(";");
                if (lagoon != null) {
                    System.out.print(tournament.getRound(roundIndex).getLagoonIndexOf(botId));
                }
            });
            System.out.println();

            System.out.print(botId + " score");
            iterate(tournament, (roundIndex, weekIndex) -> {
                Lagoon lagoon = tournament.getLagoon(roundIndex, botId, weekIndex);
                System.out.print(";");
                if (lagoon != null) {
                    System.out.print(lagoon.getScoreOf(botId));
                }
            });
            System.out.println();
        });
    }

    private static void iterate(StrategyTournament tournament, ResultConsumer consumer) {
        for (int roundIndex = 0; roundIndex < tournament.getRoundCound(); roundIndex++) {
            int weekCount = tournament.getWeekCountAt(roundIndex, 0);
            for (int weekIndex = 0; weekIndex <= weekCount; weekIndex++) {
                consumer.accept(roundIndex, weekIndex);
            }
        }
    }

    private static interface ResultConsumer {
        void accept(int roundIndex, int weekIndex);
    }
}
