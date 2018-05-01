package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.FishAction;
import com.drpicox.fishingLagoon.admin.AdminToken;
import com.drpicox.fishingLagoon.bots.Bot;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.bots.BotToken;
import com.drpicox.fishingLagoon.bots.BotsController;
import com.drpicox.fishingLagoon.common.TimeStamp;
import com.drpicox.fishingLagoon.parser.RoundParser;
import com.drpicox.fishingLagoon.rounds.Round;
import com.drpicox.fishingLagoon.rounds.RoundId;
import com.drpicox.fishingLagoon.rounds.RoundsController;

import java.sql.SQLException;
import java.util.List;

public class GameController {
    private AdminToken adminToken;
    private BotsController botsController;
    private RoundsController roundsController;
    private RoundParser roundParser;

    public GameController(AdminToken adminToken, BotsController botsController, RoundsController roundsController, RoundParser roundParser) {
        this.adminToken = adminToken;
        this.botsController = botsController;
        this.roundsController = roundsController;
        this.roundParser = roundParser;
    }

    public synchronized Bot createBot(BotToken botToken, AdminToken adminToken) throws SQLException {
        if (!this.adminToken.validate(adminToken)) return null;

        return botsController.create(botToken);
    }

    public synchronized Bot getBot(BotId botId) throws SQLException {
        return botsController.getBot(botId);
    }

    public synchronized Bot getBotByToken(BotToken botToken) throws SQLException {
        return botsController.getBotByToken(botToken);
    }

    public synchronized List<Bot> listBots() throws SQLException {
        return botsController.list();
    }

    public synchronized Bot updateBot(BotToken token, String name) throws SQLException {
        return botsController.update(token, name);
    }

    public synchronized Round createRound(String roundText, BotToken token, TimeStamp ts) throws SQLException {
        Bot bot = botsController.getBotByToken(token);
        if (bot == null) return null;

        var roundDescriptor = roundParser.parse(roundText);

        return roundsController.create(roundDescriptor, ts);
    }

    public synchronized Round getRound(RoundId id, TimeStamp ts) throws SQLException {
        return roundsController.getRound(id, ts);
    }

    public synchronized List<Round> listRounds() throws SQLException {
        return roundsController.list();
    }

    public synchronized Round seatBot(RoundId roundId, BotToken botToken, int lagoonIndex, TimeStamp ts) throws SQLException {
        Bot bot = botsController.getBotByToken(botToken);
        if (bot == null) return null;

        return roundsController.seatBot(roundId, bot.getId(), lagoonIndex, ts);
    }

    public synchronized Round commandBot(RoundId roundId, BotToken botToken, List<Action> actions, TimeStamp ts) throws SQLException {
        Bot bot = botsController.getBotByToken(botToken);
        if (bot == null) return null;

        return roundsController.commandBot(roundId, bot.getId(), actions, ts);
    }
}
