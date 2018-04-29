package com.drpicox.fishingLagoon.engine;


import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.parser.PropsParser;
import com.drpicox.fishingLagoon.parser.RoundParser;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoundCreatedTest {

    @Test
    public void round_created_gets_description() {
        var round = parse("",
                "maxDensity=4.0",
                "weekCount=10",
                "lagoons=lagoonSmall,lagoonBig",
                "lagoonSmall.fishPopulation=5",
                "lagoonBig.fishPopulation=100");

        var roundDescriptor = round.getDescriptor();

        assertThat(roundDescriptor.getMaxDensity(), is(4.0));
        assertThat(roundDescriptor.getWeekCount(), is(10));
        assertThat(roundDescriptor.getLagoonDescriptor(0).getFishPopulation(), is(5L));
        assertThat(roundDescriptor.getLagoonDescriptor(1).getFishPopulation(), is(100L));
        assertThat(roundDescriptor.getLagoonDescriptor(2).getFishPopulation(), is(5L));
    }

    private static Round parse(String... roundTextLines) {
        var roundText = String.join("\n", roundTextLines);
        var roundDescriptor = new RoundParser(new PropsParser()).parse(roundText);
        return new Round(0L, roundDescriptor);
    }

    private static BotId bot(int id) {
        return new BotId("bot" + id);
    }

    /*
    private Round makeAverageRound() {
        var tournament = parse("",
                "maxDensity=4.0",
                "lagoons=lagoonAverage",
                "weekCount=5",
                "lagoonAverage.fishPopulation=10"
        );
    }

    private Round makeSmallLargeRound() {
        var tournament = parse("",
                "maxDensity=4.0",
                "lagoons=lagoonSmall,lagoonLarge",
                "weekCount=5",
                "lagoonSmall.fishPopulation=5",
                "lagoonLarge.fishPopulation=50"
        );
    }
    */


    /*
    @Test
    public void lagoon_history_creation() {
        var tournament = parse("",
                "round[0].maxDensity=4.0",
                "round[0].lagoons=lagoonAverage",
                "round[0].weekCount=5",
                "lagoonAverage.fishPopulation=10"
                );

        var week000 = tournament.getLagoonWeek(0, 0, 0);
        var week001 = tournament.getLagoonWeek(0, 0, 1);
        var week002 = tournament.getLagoonWeek(0, 0, 2);
        var week003 = tournament.getLagoonWeek(0, 0, 3);
        var week004 = tournament.getLagoonWeek(0, 0, 4);
        var week005 = tournament.getLagoonWeek(0, 0, 5);
        var week006 = tournament.getLagoonWeek(0, 0, 6);
        var week00L = tournament.getLastLagoonWeek(0, 0);

        assertThat(tournament.getRoundCount(), is(1));
        assertThat(tournament.getFishPopulation(0), is(1));
        assertThat(tournament.getWeekCount(0), is(5));
        assertThat(week000.getFishPopulation(), is(10L));
        assertThat(week001.getFishPopulation(), is(15L));
        assertThat(week002.getFishPopulation(), is(22L));
        assertThat(week003.getFishPopulation(), is(33L));
        assertThat(week004.getFishPopulation(), is(49L));
        assertThat(week005.getFishCount(), is(73L));
        assertThat(week00L.getFishCount(), is(73L));
        assertThat(week006, is(nullValue()));
    }

    @Test
    public void tournament_timers() {
        var tournament = parse(10L, "",
                "round[0].seatNanoseconds=20",
                "round[0].commandNanoseconds=30",
                "round[0].resultNanoseconds=40",
                "round[0].maxDensity=4.0",
                "round[0].lagoons=lagoonAverage",
                "round[0].weekCount=5",
                "round[1].seatNanoseconds=50",
                "round[1].commandNanoseconds=60",
                "round[1].resultNanoseconds=70",
                "round[1].maxDensity=4.0",
                "round[1].lagoons=lagoonAverage",
                "round[1].weekCount=5",
                "lagoonAverage.fishPopulation=10"
        );

        assertThat(tournament.getRoundStateAt(0, 0L), is(CREATED));
        assertThat(tournament.getRoundStateAt(0, 0L).isDescriptorReadable(), is(false));
        assertThat(tournament.getRoundStateAt(0, 9L), is(CREATED));
        assertThat(tournament.getRoundStateAt(0, 10L), is(SEATING));
        assertThat(tournament.getRoundStateAt(0, 10L).isDescriptorReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 10L).isSeatsReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 10L).isAcceptingSeats(), is(true));
        assertThat(tournament.getRoundStateAt(0, 10L).isAcceptingCommands(), is(false));
        assertThat(tournament.getRoundStateAt(0, 29L), is(SEATING));
        assertThat(tournament.getRoundStateAt(0, 30L), is(COMMANDING));
        assertThat(tournament.getRoundStateAt(0, 30L).isDescriptorReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 30L).isSeatsReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 30L).isAcceptingCommands(), is(true));
        assertThat(tournament.getRoundStateAt(0, 30L).isCommandsReadable(), is(false));
        assertThat(tournament.getRoundStateAt(0, 30L).isAcceptingSeats(), is(false));
        assertThat(tournament.getRoundStateAt(0, 59L), is(COMMANDING));
        assertThat(tournament.getRoundStateAt(0, 60L), is(SCORING));
        assertThat(tournament.getRoundStateAt(0, 60L).isDescriptorReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 60L).isSeatsReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 60L).isCommandsReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 60L).isScoresReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 60L).isAcceptingCommands(), is(false));
        assertThat(tournament.getRoundStateAt(0, 60L).isAcceptingSeats(), is(false));
        assertThat(tournament.getRoundStateAt(0, 99L), is(SCORING));
        assertThat(tournament.getRoundStateAt(0, 100L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(0, 100L).isDescriptorReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 100L).isScoresReadable(), is(true));
        assertThat(tournament.getRoundStateAt(0, 100L).isSeatsReadable(), is(false));
        assertThat(tournament.getRoundStateAt(0, 100L).isCommandsReadable(), is(false));
        assertThat(tournament.getRoundStateAt(0, 100L).isAcceptingCommands(), is(false));
        assertThat(tournament.getRoundStateAt(0, 100L).isAcceptingSeats(), is(false));
        assertThat(tournament.getRoundStateAt(0, 200L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(0, 5000L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(1, 0L), is(CREATED));
        assertThat(tournament.getRoundStateAt(1, 99L), is(CREATED));
        assertThat(tournament.getRoundStateAt(1, 100L), is(SEATING));
        assertThat(tournament.getRoundStateAt(1, 149L), is(SEATING));
        assertThat(tournament.getRoundStateAt(1, 150L), is(COMMANDING));
        assertThat(tournament.getRoundStateAt(1, 209L), is(COMMANDING));
        assertThat(tournament.getRoundStateAt(1, 210L), is(SCORING));
        assertThat(tournament.getRoundStateAt(1, 279L), is(SCORING));
        assertThat(tournament.getRoundStateAt(1, 280L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(1, 500L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(1, 5000L), is(FINISHED));
        assertThat(tournament.getRoundStateAt(2, 0L), is(UNEXISTING));
        assertThat(tournament.getRoundStateAt(2, 0L).isDescriptorReadable(), is(false));
        assertThat(tournament.getRoundStateAt(2, 0L).isScoresReadable(), is(false));
        assertThat(tournament.getRoundStateAt(2, 0L).isSeatsReadable(), is(false));
        assertThat(tournament.getRoundStateAt(2, 0L).isCommandsReadable(), is(false));
        assertThat(tournament.getRoundStateAt(2, 0L).isAcceptingCommands(), is(false));
        assertThat(tournament.getRoundStateAt(2, 0L).isAcceptingSeats(), is(false));
        assertThat(tournament.getStartTs(), is(10L));
        assertThat(tournament.getFinishedTs(), is(280L));
        assertThat(tournament.isReady(0L), is(true));
        assertThat(tournament.isReady(9L), is(true));
        assertThat(tournament.isReady(10L), is(false));
        assertThat(tournament.isReady(280L), is(false));
        assertThat(tournament.isRunning(0L), is(false));
        assertThat(tournament.isRunning(9L), is(false));
        assertThat(tournament.isRunning(10L), is(true));
        assertThat(tournament.isRunning(279L), is(true));
        assertThat(tournament.isRunning(280L), is(false));
        assertThat(tournament.isFinished(0L), is(false));
        assertThat(tournament.isFinished(279L), is(false));
        assertThat(tournament.isFinished(280L), is(true));
    }

    @Test
    public void tournament_round_descriptors() {
        var tournament = parse("",
                "round[0].maxDensity=4.0",
                "round[0].lagoons=lagoonAverage",
                "round[0].weekCount=5",
                "round[1].maxDensity=5.0",
                "round[1].lagoons=lagoonSmall,lagoonAverage",
                "round[1].weekCount=5",
                "lagoonSmall.fishCount=5",
                "lagoonAverage.fishCount=10"
        );

        var roundDescriptor0 = tournament.getRoundDescriptor(0);
        var roundDescriptor1 = tournament.getRoundDescriptor(1);
        assertThat(roundDescriptor0.getMaxDensity(), is(4.0));
        assertThat(roundDescriptor0.getFishPopulation(), is(1));
        assertThat(roundDescriptor0.getLagoonDescriptor(0).getFishCount(), is(10L));
        assertThat(roundDescriptor1.getMaxDensity(), is(5.0));
        assertThat(roundDescriptor1.getFishPopulation(), is(2));
        assertThat(roundDescriptor1.getLagoonDescriptor(0).getFishCount(), is(5L));
        assertThat(roundDescriptor1.getLagoonDescriptor(1).getFishCount(), is(10L));
    }

    @Test
    public void tournament_seats() {
        var tournament = parse("",
                "round[0].maxDensity=4.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=5",
                "lagoonAverage.fishCount=10"
        );

        var prevSeat01 = tournament.seatBot(botId1, 0, 0);
        var prevSeat02 = tournament.seatBot(botId2, 0, 0);
        var prevSeat03 = tournament.seatBot(botId3, 0, 2);
        var roundSeats0 = tournament.getSeats(0);
        var prevSeat11 = tournament.seatBot(botId1, 0, 1);
        var prevSeat12 = tournament.seatBot(botId2, 0, 0);
        var prevSeat13 = tournament.seatBot(botId3, 0, 0);
        var roundSeats1 = tournament.getSeats(0);
        var prevSeat23 = tournament.seatBot(botId3, 0, 2);
        var roundSeats2 = tournament.getSeats(0);
        var prevSeat33 = tournament.seatBot(botId3, 0, 1);
        var roundSeats3 = tournament.getSeats(0);

        assertThat(prevSeat01, is(-1));
        assertThat(prevSeat02, is(-1));
        assertThat(prevSeat03, is(-1));
        assertThat(roundSeats0.getBotSeat(botId1), is(0));
        assertThat(roundSeats0.getBotSeat(botId2), is(0));
        assertThat(roundSeats0.getBotSeat(botId3), is(-1));
        assertThat(prevSeat11, is(0));
        assertThat(prevSeat12, is(0));
        assertThat(prevSeat13, is(-1));
        assertThat(roundSeats1.getBotSeat(botId1), is(1));
        assertThat(roundSeats1.getBotSeat(botId2), is(0));
        assertThat(roundSeats1.getBotSeat(botId3), is(0));
        assertThat(prevSeat23, is(0));
        assertThat(roundSeats2.getBotSeat(botId3), is(0));
        assertThat(prevSeat33, is(0));
        assertThat(roundSeats3.getBotSeat(botId3), is(1));
    }

    @Test
    public void tournament_seats_not_increases_if_room_for_1_more() {
        var tournament = parse("",
                "round[0].maxDensity=1.0",
                "round[0].lagoons=lagoonSmall,lagoonBig",
                "round[0].weekCount=5",
                "lagoonSmall.fishCount=5",
                "lagoonBig.fishCount=100"
        );

        tournament.seatBot(botId1, 0, 0);

        var lagoonCount = tournament.getFishPopulation(0);
        assertThat(lagoonCount, is(2));
    }

    @Test
    public void tournament_seats_increases_if_not_room_for_1_more() {
        var tournament = parse("",
                "round[0].maxDensity=1.0",
                "round[0].lagoons=lagoonSmall,lagoonBig",
                "round[0].weekCount=5",
                "lagoonSmall.fishCount=5",
                "lagoonBig.fishCount=100"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 0);

        var lagoonCount = tournament.getFishPopulation(0);
        assertThat(lagoonCount, is(3));
    }

    @Test
    public void tournament_seats_increases_add_lagoons_module_basis() {
        var tournament = parse("",
                "round[0].maxDensity=1.0",
                "round[0].lagoons=lagoonSmall,lagoonBig",
                "round[0].weekCount=5",
                "lagoonSmall.fishCount=5",
                "lagoonBig.fishCount=100"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 0);
        tournament.seatBot(botId3, 0, 0);

        var lagoonCount = tournament.getFishPopulation(0);
        var fishCount2 = tournament.getLagoonWeek(0, 2, 0).getFishCount();
        var fishCount4 = tournament.getLagoonWeek(0, 3, 0).getFishCount();
        assertThat(lagoonCount, is(4));
        assertThat(fishCount2, is(5L));
        assertThat(fishCount4, is(100L));
    }

    @Test
    public void tournament_seats_increase_works_well_with_decimal_densities() {
        var tournament = parse("",
                "round[0].maxDensity=1.5",
                "round[0].lagoons=lagoonAverage",
                "round[0].weekCount=5",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 0);

        var lagoonCount = tournament.getFishPopulation(0);
        assertThat(lagoonCount, is(2));
    }

    @Test
    public void tournament_commands_updates_weeks() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=2",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 1);
        tournament.seatBot(botId3, 0, 1);
        tournament.commandBot(botId1, 0, fish(1), fish(2));
        tournament.commandBot(botId2, 0, fish(2), fish(1));
        tournament.commandBot(botId3, 0, fish(3), fish(1));

        var week000 = tournament.getLagoonWeek(0, 0, 0);
        var week001 = tournament.getLagoonWeek(0, 0, 1);
        var week002 = tournament.getLagoonWeek(0, 0, 2);
        var week010 = tournament.getLagoonWeek(0, 1, 0);
        var week011 = tournament.getLagoonWeek(0, 1, 1);
        var week012 = tournament.getLagoonWeek(0, 1, 2);

        assertThat(week000.getFishCount(), is(10L));
        assertThat(week001.getFishCount(), is(13L));
        assertThat(week002.getFishCount(), is(16L));
        assertThat(week010.getFishCount(), is(10L));
        assertThat(week011.getFishCount(), is(7L));
        assertThat(week012.getFishCount(), is(7L));
    }

    @Test
    public void tournament_commands_updates_commands() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=2",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 1);
        tournament.seatBot(botId3, 0, 1);
        tournament.commandBot(botId1, 0, fish(1), fish(2));
        tournament.commandBot(botId2, 0, fish(2), fish(1));
        tournament.commandBot(botId3, 0, fish(3), fish(1));
    }

    @Test
    public void tournament_commands_updates_results() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=2",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.seatBot(botId2, 0, 1);

        tournament.commandBot(botId1, 0, fish(1), fish(20));
        tournament.commandBot(botId2, 0, fish(2), fish(20));
        tournament.commandBot(botId3, 0, fish(3), fish(20));

        var roundCommands = tournament.getCommands(0);
        assertThat(roundCommands.get(botId1), is(arrayContaining(fish(1), fish(20))));
        assertThat(roundCommands.get(botId2), is(arrayContaining(fish(2), fish(20))));
        assertThat(roundCommands.get(botId3), is(nullValue()));
        assertThat(roundCommands.getBots(), containsInAnyOrder(botId1, botId2));
    }

    @Test(expected = IllegalStateException.class)
    public void tournament_constrain_cannot_seat_after_command() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=2",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.commandBot(botId1, 0, fish(1), fish(20));

        tournament.seatBot(botId2, 0, 1);
    }

    @Test
    public void tournament_constraing_allow_change_history() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage,lagoonAverage",
                "round[0].weekCount=1",
                "lagoonAverage.fishCount=10"
        );

        assertThat(tournament.getLagoonWeek(0, 0, 1).getFishCount(), is(15L));
        assertThat(tournament.getScores(0).get(botId1), is(0L));
        assertThat(tournament.getTournamentScores().get(botId1), is(0L));

        tournament.seatBot(botId1, 0, 0);
        tournament.commandBot(botId1, 0, fish(1));

        assertThat(tournament.getLagoonWeek(0, 0, 1).getFishCount(), is(13L));
        assertThat(tournament.getScores(0).get(botId1), is(1L));
        assertThat(tournament.getTournamentScores().get(botId1), is(1L));

        tournament.commandBot(botId1, 0, fish(8));

        assertThat(tournament.getLagoonWeek(0, 0, 1).getFishCount(), is(3L));
        assertThat(tournament.getScores(0).get(botId1), is(8L));
        assertThat(tournament.getTournamentScores().get(botId1), is(8L));
    }

    @Test
    public void tournament_scores() {
        var tournament = parse("",
                "round[0].maxDensity=5.0",
                "round[0].lagoons=lagoonAverage",
                "round[0].weekCount=2",
                "round[1].maxDensity=5.0",
                "round[1].lagoons=lagoonAverage",
                "round[1].weekCount=2",
                "lagoonAverage.fishCount=10"
        );

        tournament.seatBot(botId1, 0, 0);
        tournament.commandBot(botId1, 0, fish(1), fish(2));
        tournament.commandBot(botId2, 0, fish(2), fish(3));

        tournament.seatBot(botId1, 1, 0);
        tournament.seatBot(botId2, 1, 0);
        tournament.commandBot(botId1, 1, fish(1), fish(3));
        tournament.commandBot(botId2, 1, fish(10), rest());
        tournament.commandBot(botId3, 1, fish(1), rest());

        var roundScores0 = tournament.getScores(0);
        var roundScores1 = tournament.getScores(1);
        var tournamentScores = tournament.getTournamentScores();

        assertThat(roundScores0.get(botId1), is(3L));
        assertThat(roundScores0.get(botId2), is(0L));
        assertThat(roundScores0.get(botId3), is(0L));
        assertThat(roundScores0.getBots(), containsInAnyOrder(botId1));
        assertThat(roundScores1.get(botId1), is(1L));
        assertThat(roundScores1.get(botId2), is(9L));
        assertThat(roundScores1.get(botId3), is(0L));
        assertThat(roundScores1.getBots(), containsInAnyOrder(botId1, botId2));
        assertThat(tournamentScores.get(botId1), is(4L));
        assertThat(tournamentScores.get(botId2), is(9L));
        assertThat(tournamentScores.get(botId3), is(0L));
        assertThat(tournamentScores.getBots(), containsInAnyOrder(botId1, botId2));
    }


    private static Tournament parse( String... tournamentTextLines) {
        return parse(0L, tournamentTextLines);
    }

    private static Tournament parse(long startTs, String... tournamentTextLines) {
        var tournamentDescriptor = new RoundParser().parse(String.join("\n", tournamentTextLines));
        var tournament = new Tournament(new FishingLagoonRules(
                new FishingLagoonRuleFishing(),
                new FishingLagoonRuleProcreation()
        ), startTs, tournamentDescriptor);
        return tournament;
    }
    */


}
