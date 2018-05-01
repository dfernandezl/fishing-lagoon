package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.admin.AdminToken;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.bots.BotToken;
import com.drpicox.fishingLagoon.common.TimeStamp;
import com.drpicox.fishingLagoon.rounds.RoundId;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static spark.Spark.*;

public class RestController {

    private ActionParser actionParser;
    private GameController gameController;
    private Gson gson;

    public RestController(ActionParser actionParser, GameController gameController, Gson gson) {
        this.actionParser = actionParser;
        this.gameController = gameController;
        this.gson = gson;
    }

    public void start() {
        get("/hello", (rq, rs) -> {
           var m = new HashMap();
           m.put("hello", "orld");
           return m;
        }, gson::toJson);

        get("/bots", this::listBots, gson::toJson);
        post("/bots", this::createBot, gson::toJson);
        get("/bots/:botId", this::getBot, gson::toJson);
        put("/bots/:botToken", this::updateBot, gson::toJson);

        post("/rounds", this::createRound, gson::toJson);
        get("/rounds", this::listRounds, gson::toJson);
        get("/rounds/:roundId", this::getRound, gson::toJson);
        put("/rounds/:roundId/seats/:botToken", this::seatBot, gson::toJson);
        put("/rounds/:roundId/commands/:botToken", this::commandBot, gson::toJson);

        exception(IllegalArgumentException.class, this::handle);
        exception(IllegalStateException.class, this::handle);
        exception(SQLException.class, this::handle);
    }

    private <T extends Throwable> void handle(T error, Request request, Response response) {
        response.status(400);
        response.body(error.getClass().getSimpleName() + ": " + error.getMessage() + ".");
    }

    private Object commandBot(Request request, Response response) throws SQLException {
        var roundId = new RoundId(request.params("roundId"));
        var botToken = new BotToken(request.params("botToken"));
        var actions = actionParser.parse(request.body());
        return gameController.commandBot(roundId, botToken, actions, now());
    }

    private Object seatBot(Request request, Response response) throws SQLException {
        var roundId = new RoundId(request.params("roundId"));
        var botToken = new BotToken(request.params("botToken"));
        var lagoonIndex = Integer.parseInt(request.body());
        return gameController.seatBot(roundId, botToken, lagoonIndex, now());
    }

    private Object getRound(Request request, Response response) throws SQLException {
        var roundId = new RoundId(request.params("roundId"));
        return gameController.getRound(roundId, now());
    }

    private Object listRounds(Request request, Response response) throws SQLException {
        return gameController.listRounds();
    }

    private Object createRound(Request request, Response response) throws SQLException {
        var botToken = new BotToken(request.queryParams("botToken"));
        var roundText = request.body();
        return gameController.createRound(roundText, botToken, now());
    }

    private TimeStamp now() {
        return new TimeStamp(System.currentTimeMillis());
    }

    private Object listBots(Request request, Response response) throws SQLException {
        return gameController.listBots();
    }

    private Object createBot(Request request, Response response) throws SQLException {
        var botToken = new BotToken(request.queryParams("botToken"));
        var adminToken = new AdminToken(request.queryParams("adminToken"));
        return gameController.createBot(botToken, adminToken);
    }

    private Object getBot(Request request, Response response) throws SQLException {
        var botId = new BotId(request.params("botId"));
        return gameController.getBot(botId);
    }

    private Object updateBot(Request request, Response response) throws SQLException {
        var botToken = new BotToken(request.params("botToken"));
        var map = gson.fromJson(request.body(), Map.class);
        return gameController.updateBot(botToken, (String) map.get("name"));
    }



}
