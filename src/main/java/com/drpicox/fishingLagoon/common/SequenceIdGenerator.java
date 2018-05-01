package com.drpicox.fishingLagoon.common;

public class SequenceIdGenerator implements IdGenerator {
    private String type;
    private int next = 1;

    public SequenceIdGenerator(String type) {
        this.type = type;
    }

    @Override
    public String next() {
        int current = next;
        next += 1;
        return type + current;
    }
}
