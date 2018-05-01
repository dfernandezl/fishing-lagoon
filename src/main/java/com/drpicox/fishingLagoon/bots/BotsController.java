package com.drpicox.fishingLagoon.bots;

import com.drpicox.fishingLagoon.common.IdGenerator;

import java.sql.SQLException;
import java.util.List;

public class BotsController {
    private BotsStore botsStore;
    private IdGenerator idGenerator;

    public BotsController(BotsStore botsStore, IdGenerator idGenerator) {
        this.botsStore = botsStore;
        this.idGenerator = idGenerator;
    }

    public Bot create(BotToken botToken) throws SQLException {
        var bot = new Bot(new BotId(idGenerator.next()));
        return botsStore.create(botToken, bot);
    }

    public Bot getBot(BotId id) throws SQLException {
        return botsStore.get(id);
    }

    public Bot getBotByToken(BotToken token) throws SQLException {
        return botsStore.getByToken(token);
    }

    public List<Bot> list() throws SQLException {
        return botsStore.list();
    }

    public Bot update(BotToken token, String name) throws SQLException {
        var bot = botsStore.getByToken(token);
        bot.update(name);
        return botsStore.save(bot);
    }
}
