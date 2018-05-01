package com.drpicox.fishingLagoon.bots;

public class BotToken {

    private String value;

    public BotToken(String value) {
        this.value = value;
    }

    private boolean validate(BotToken other) {
        return value.equals(other.value);
    }

    public String getValue() {
        return value;
    }
}
