package com.drpicox.fishingLagoon.rounds;

import com.drpicox.fishingLagoon.common.TimeStamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RoundsStore {
    private Connection connection;

    public RoundsStore(Connection connection) throws SQLException {
        this.connection = connection;

        try (var stmt = connection.createStatement()) {
            stmt.execute("" +
                    "CREATE TABLE IF NOT EXISTS rounds (" +
                    "  id VARCHAR(255) PRIMARY KEY," +
                    "  startTs DECIMAL(20)," +
                    "  endTs DECIMAL(20)" +
                    ")"
            );
        }
    }

    public Round get(RoundId id) throws SQLException {
        try (var pstmt = this.connection.prepareStatement("SELECT * FROM rounds WHERE id = ?")) {
            pstmt.setString(1, id.getValue());
            try (var rs = pstmt.executeQuery()) {
                return nextRound(rs);
            }
        }
    }

    public List<Round> list() throws SQLException {
        try (var pstmt = this.connection.prepareStatement("SELECT * FROM rounds")) {
            return nextRounds(pstmt);
        }
    }

    public List<Round> listSince(TimeStamp ts) throws SQLException {
        try (var pstmt = this.connection.prepareStatement("SELECT * FROM rounds WHERE ? <= endTs")) {
            pstmt.setLong(1, ts.getMilliseconds());
            return nextRounds(pstmt);
        }
    }

    private List<Round> nextRounds(PreparedStatement pstmt) throws SQLException {
        var result = new LinkedList<Round>();

        try (var rs = pstmt.executeQuery()) {
            Round bot = nextRound(rs);
            while (bot != null) {
                result.add(bot);
                bot = nextRound(rs);
            }
        }

        return result;
    }

    public Round save(Round round) throws SQLException {
        try (var pstmt = connection.prepareStatement(
                "MERGE INTO rounds(id, startTs, endTs) VALUES(?, ?, ?)")
        ) {
            pstmt.setString(1, round.getId().getValue());
            pstmt.setLong(2, round.getStartTs().getMilliseconds());
            pstmt.setLong(3, round.getEndTs().getMilliseconds());
            pstmt.execute();
        };

        return round;
    }

    private Round nextRound(ResultSet rs) throws SQLException {
        if (rs.next()) {
            String id = rs.getString("id");
            long startTs = rs.getLong("startTs");
            long endTs = rs.getLong("endTs");
            return new Round(
                    new RoundId(id),
                    new TimeStamp(startTs),
                    new TimeStamp(endTs)
            );
        } else {
            return null;
        }
    }
}
