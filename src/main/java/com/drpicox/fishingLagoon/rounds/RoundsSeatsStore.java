package com.drpicox.fishingLagoon.rounds;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.engine.RoundEngine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class RoundsSeatsStore {
    private Connection connection;

    public RoundsSeatsStore(Connection connection) throws SQLException {
        this.connection = connection;

        try (var stmt = connection.createStatement()) {
            stmt.execute("" +
                    "CREATE TABLE IF NOT EXISTS roundsSeats (" +
                    "  id VARCHAR(255)," +
                    "  botId VARCHAR(255)," +
                    "  lagoonIndex DECIMAL(20)," +
                    "  PRIMARY KEY (id, botId)" +
                    ")"
            );
        }
    }

    public void get(RoundId id, RoundEngine engine) throws SQLException {
        try (var pstmt = this.connection.prepareStatement("SELECT * FROM roundsSeats WHERE id = ?")) {
            pstmt.setString(1, id.getValue());
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    var botId = new BotId(rs.getString("botId"));
                    var lagoonIndex = rs.getInt("lagoonIndex");
                    engine.forceSeatBot(botId, lagoonIndex);
                }
            }
        }
    }

    public void save(RoundId id, BotId botId, int lagoonIndex) throws SQLException {
        try (var pstmt = connection.prepareStatement(
                "MERGE INTO roundsSeats(id, botId, lagoonIndex) VALUES(?, ?, ?)")
        ) {
            pstmt.setString(1, id.getValue());
            pstmt.setString(2, botId.getValue());
            pstmt.setInt(3, lagoonIndex);
            pstmt.execute();
        };
    }
}
