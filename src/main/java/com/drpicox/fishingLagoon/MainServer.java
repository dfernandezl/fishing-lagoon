package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.admin.AdminToken;

import java.sql.SQLException;

public class MainServer {

    public static void main(String[] args) throws SQLException {
        var adminToken = System.getenv("FISHING_LAGOON_ADMIN_TOKEN");

        var bootstrap = new Bootstrap(new AdminToken(adminToken));
        var restController = bootstrap.getRestController();
        restController.start();
    }
}
