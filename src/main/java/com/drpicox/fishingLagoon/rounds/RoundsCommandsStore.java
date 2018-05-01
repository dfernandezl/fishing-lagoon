package com.drpicox.fishingLagoon.rounds;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.engine.RoundEngine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoundsCommandsStore {
    private ActionParser actionParser;
    private Connection connection;

    public RoundsCommandsStore(ActionParser actionParser, Connection connection) throws SQLException {
        this.actionParser = actionParser;
        this.connection = connection;

        try (var stmt = connection.createStatement()) {
            stmt.execute("" +
                    "CREATE TABLE IF NOT EXISTS roundsCommands (" +
                    "  id VARCHAR(255)," +
                    "  botId VARCHAR(255)," +
                    "  actionsText TEXT," +
                    "  PRIMARY KEY (id, botId)" +
                    ")"
            );
        }
    }

    public void get(RoundId id, RoundEngine engine) throws SQLException {
        try (var pstmt = this.connection.prepareStatement("SELECT * FROM roundsCommands WHERE id = ?")) {
            pstmt.setString(1, id.getValue());
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    var botId = new BotId(rs.getString("botId"));
                    var actionsText = rs.getString("actionsText");
                    var actions = actionParser.parse(actionsText);
                    engine.forceCommandBot(botId, actions);
                }
            }
        }
    }

    public void save(RoundId id, BotId botId, List<Action> actions) throws SQLException {
        try (var pstmt = connection.prepareStatement(
                "MERGE INTO roundsCommands(id, botId, actionsText) VALUES(?, ?, ?)")
        ) {
            String actionsText = actionParser.toString(actions);
            pstmt.setString(1, id.getValue());
            pstmt.setString(2, botId.getValue());
            pstmt.setString(3, actionsText);
            var result = pstmt.executeUpdate();
        };
    }
}
